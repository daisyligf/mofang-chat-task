package com.mofang.chat.task.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mofang.chat.task.component.GrowupComponent;
import com.mofang.chat.task.component.MedalComponent;
import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.global.common.TaskType;
import com.mofang.chat.task.model.Task;
import com.mofang.chat.task.model.UserTask;
import com.mofang.chat.task.mysql.TaskDao;
import com.mofang.chat.task.mysql.UserTaskDao;
import com.mofang.chat.task.mysql.impl.TaskDaoImpl;
import com.mofang.chat.task.mysql.impl.UserTaskDaoImpl;
import com.mofang.chat.task.redis.TaskRedis;
import com.mofang.chat.task.redis.UserTaskDailyRedis;
import com.mofang.chat.task.redis.UserTaskOnetimeRedis;
import com.mofang.chat.task.redis.impl.TaskRedisImpl;
import com.mofang.chat.task.redis.impl.UserTaskDailyRedisImpl;
import com.mofang.chat.task.redis.impl.UserTaskOnetimeRedisImpl;
import com.mofang.chat.task.service.UserService;
import com.mofang.chat.task.service.UserTaskService;
import com.mofang.framework.net.http.HttpClientSender;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class UserTaskServiceImpl implements UserTaskService
{
	private final static UserTaskServiceImpl SERVICE = new UserTaskServiceImpl();
	private final static int ONETIME_TASKDATE = 1;
	private TaskRedis taskRedis = TaskRedisImpl.getInstance();
	private UserTaskDailyRedis dailyTaskRedis = UserTaskDailyRedisImpl.getInstance();
	private UserTaskOnetimeRedis onetimeTaskRedis = UserTaskOnetimeRedisImpl.getInstance();
	private UserTaskDao userTaskDao = UserTaskDaoImpl.getInstance();
	private TaskDao taskDao = TaskDaoImpl.getInstance();
	private UserService userService = UserServiceImpl.getInstance();
	
	private UserTaskServiceImpl()
	{}
	
	public static UserTaskServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public boolean execute(long userId, int event) throws Exception
	{
		TaskExecutor executor = new TaskExecutor(userId, event);
		GlobalObject.TASK_EXECUTOR.execute(executor);
		return true;
	}
	
	@Override
	public JSONArray getTaskList(long userId) throws Exception
	{
		///获取所有的任务列表
		List<Task> taskList = taskRedis.getList();
		if(null == taskList)
			return null;
		
		///获取用户执行的任务列表(一次性和日常的)
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		int taskDate = Integer.parseInt(dateFormat.format(new Date()));
		Map<Integer, Long> dailyTaskMap = dailyTaskRedis.getCompletedCountMap(userId, taskDate);
		Map<Integer, Long> onetimeTaskMap = onetimeTaskRedis.getCompletedCountMap(userId);
		
		JSONArray data = new JSONArray();
		JSONObject taskJson = null;
		boolean isCompleted = false;
		for(Task taskInfo : taskList)
		{
			taskJson = new JSONObject();
			taskJson.put("task_id", taskInfo.getTaskId());
			taskJson.put("name", taskInfo.getTaskName());
			taskJson.put("type", taskInfo.getType());
			taskJson.put("description", taskInfo.getDescription());
			taskJson.put("limit_level", taskInfo.getLimitLevel());
			taskJson.put("limit_times", taskInfo.getLimitTimes());
			taskJson.put("event_param", taskInfo.getEventParam());
			
			///计算任务参数完成数
			int completedCount = 0;
			if(taskInfo.getType() == TaskType.DAILY)
			{
				if(null != dailyTaskMap && dailyTaskMap.containsKey(taskInfo.getTaskId()))
					completedCount = dailyTaskMap.get(taskInfo.getTaskId()).intValue();
			}
			else if(taskInfo.getType() == TaskType.ONETIME)
			{
				if(null != onetimeTaskMap && onetimeTaskMap.containsKey(taskInfo.getTaskId()))
					completedCount = onetimeTaskMap.get(taskInfo.getTaskId()).intValue();
			}
			
			///判断任务是否已完成
			int needCompletedCount = taskInfo.getEventParam() * taskInfo.getLimitTimes();
			isCompleted = (completedCount >= needCompletedCount);
			taskJson.put("is_completed", isCompleted);
			taskJson.put("completed_count", completedCount > needCompletedCount ? needCompletedCount : completedCount);
			
			JSONObject rewardJson = new JSONObject();
			rewardJson.put("coin", taskInfo.getRewardCoin());
			rewardJson.put("diamond", taskInfo.getRewardDiamond());
			rewardJson.put("exp", taskInfo.getRewardExp());
			taskJson.put("reward", rewardJson);
			data.put(taskJson);
		}
		return data;
	}
	
	@Override
	public long getTaskCompletedCount(long userId, int medalEvent) throws Exception
	{
		Task task = taskDao.getTaskByMedalEvent(medalEvent);
		if(null == task)
			return 0L;
		
		int taskId = task.getTaskId();
		int taskType = task.getType();
		if(taskType != TaskType.ONETIME)
			return 0L;
		
		return onetimeTaskRedis.getCompletedCount(userId, taskId);
	}
	
	class TaskExecutor implements Runnable
	{
		private long userId;
		private int event;
		
		public TaskExecutor(long userId, int event)
		{
			this.userId = userId;
			this.event = event;
		}
		
		@Override
		public void run() 
		{
			try
			{
				List<Task> taskList = taskDao.getListByEvent(event);
				if(null == taskList || taskList.size() == 0)
					return;
			
				for(Task taskInfo : taskList)
				{
					execSingleTask(taskInfo);
				}
			}
			catch(Exception e)
			{
				GlobalObject.ERROR_LOG.error("at TaskExecutor.run throw an error. ", e);
			}
		}
		
		private void execSingleTask(Task taskInfo)
		{
			try
			{
				int limitLevel = taskInfo.getLimitLevel();
				if(0 < limitLevel)   ///0代表不限制等级
				{
					int userLevel = userService.getUserLevel(userId);
					if(userLevel < limitLevel)
						return;
				}
				
				int needCompletedCount = taskInfo.getEventParam() * taskInfo.getLimitTimes();
				long completedCount = 0L;
				int taskDate = 0;
				boolean needReward = false;
				boolean isCompleted = false;
				
				UserTask userTaskInfo = new UserTask();
				userTaskInfo.setUserId(userId);
				userTaskInfo.setTaskId(taskInfo.getTaskId());
				
				if(taskInfo.getType() == TaskType.DAILY)
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					taskDate = Integer.parseInt(dateFormat.format(new Date()));
					completedCount = dailyTaskRedis.incr(userId, taskInfo.getTaskId(), taskDate);
				}
				else if(taskInfo.getType() == TaskType.ONETIME)
				{
					taskDate = ONETIME_TASKDATE;
					completedCount = onetimeTaskRedis.incr(userId, taskInfo.getTaskId());
				}
				
				isCompleted = completedCount >= needCompletedCount;
				userTaskInfo.setTaskDate(taskDate);
				userTaskInfo.setCompletedCount(Long.valueOf(completedCount).intValue());
				userTaskInfo.setIsCompleted(isCompleted);
				userTaskInfo.setCompletedTime(new Date());
				
				if(completedCount <= 1)   ///还没执行过该任务
				{
					///将用户任务添加到mysql中
					userTaskDao.add(userTaskInfo);
				}
				else
				{
					///将用户任务更新到mysql中
					userTaskDao.update(userTaskInfo);
				}
				
				///判断是否需要给予奖励(魔币，魔钻，经验等)
				needReward = (completedCount <= needCompletedCount) && ((completedCount % taskInfo.getEventParam()) == 0);
				if(needReward)
				{
					///如果勋章事件>0，则请求勋章系统，反之请求成长系统
					if(taskInfo.getMedalEvent() > 0)
					{
						reqMedal(userId, taskInfo.getMedalEvent());
					}
					else
					{
						///请求成长系统
						JSONObject dataJson = reqGrowup(taskInfo);
						if(null != dataJson)
						{
							///构建push通知JSON对象
							JSONObject msgJson = new JSONObject();
							msgJson.put("task_id", taskInfo.getTaskId());
							msgJson.put("name", taskInfo.getTaskName());
							msgJson.put("type", taskInfo.getType());
							msgJson.put("description", taskInfo.getDescription());
							msgJson.put("limit_level", taskInfo.getLimitLevel());
							msgJson.put("limit_times", taskInfo.getLimitTimes());
							msgJson.put("event_param", taskInfo.getEventParam());
							msgJson.put("reward_coin", taskInfo.getRewardCoin());
							msgJson.put("reward_exp", taskInfo.getRewardExp());
							msgJson.put("completed_count", completedCount > needCompletedCount ? needCompletedCount : completedCount);
							msgJson.put("is_completed", isCompleted);
							JSONObject userJson = new JSONObject();
							userJson.put("uid", dataJson.optLong("uid", 0L));
							userJson.put("coin", dataJson.optDouble("coin", 0));
							userJson.put("diamond", dataJson.optDouble("diamond", 0L));
							userJson.put("gained_exp", dataJson.optInt("gained_exp", 0));
							userJson.put("upgrade_exp", dataJson.optInt("upgrade_exp", 0));
							userJson.put("level", dataJson.optInt("level", 0));
							userJson.put("is_upgrade", dataJson.optBoolean("is_upgrade", false));
							msgJson.put("user", userJson);
							
							pushNotify(msgJson);
						}
					}
				}
			}
			catch(Exception e)
			{
				GlobalObject.ERROR_LOG.error("at TaskExecutor.execSingleTask throw an error. ", e);
			}
		}
		
		private JSONObject reqGrowup(Task taskInfo)
		{
			try
			{
				String result = GrowupComponent.increment(userId, taskInfo.getRewardCoin(), taskInfo.getRewardDiamond(), taskInfo.getRewardExp());
				if(StringUtil.isNullOrEmpty(result))
				{
					GlobalObject.ERROR_LOG.error("at TaskExecutor.reqGrowup throw an error, message: response is null or empty.");
					return null;
				}
				
				JSONObject responseJson = new JSONObject(result);
				int code = responseJson.optInt("code", -1);
				if(0 != code) 
				{
					GlobalObject.ERROR_LOG.error("at TaskExecutor.reqGrowup throw an error, message: server return an error code." + responseJson.toString());
					return null;
				}
				
				JSONObject data = responseJson.optJSONObject("data");
				if(null == data)
				{
					GlobalObject.ERROR_LOG.error("at TaskExecutor.reqGrowup throw an error, message: server return an null data." + responseJson.toString());
					return null;
				}
				
				GlobalObject.INFO_LOG.info("growup api response:" + responseJson.toString());
				return data;
			}
			catch(Exception e)
			{
				GlobalObject.ERROR_LOG.error("TaskExecutor.reqGrowup throw an error. ", e);
				return null;
			}
		}
		
		private void reqMedal(long userId, int medalEvent)
		{
			try
			{
				String result = MedalComponent.completed(userId, medalEvent);
				if(StringUtil.isNullOrEmpty(result))
				{
					GlobalObject.ERROR_LOG.error("at TaskExecutor.reqMedal throw an error, message: response is null or empty.");
					return;
				}
				
				JSONObject responseJson = new JSONObject(result);
				int code = responseJson.optInt("code", -1);
				if(0 != code) 
				{
					GlobalObject.ERROR_LOG.error("at TaskExecutor.reqMedal throw an error, message: server return an error code." + responseJson.toString());
					return;
				}
				
				GlobalObject.INFO_LOG.info("medal api response:" + responseJson.toString());
			}
			catch(Exception e)
			{
				GlobalObject.ERROR_LOG.error("TaskExecutor.reqMedal throw an error. ", e);
				return;
			}
		}
		
		private void pushNotify(JSONObject msgJson)
		{
			try
			{
				JSONObject pushJson = new JSONObject();
				pushJson.put("act", "push_task");
				pushJson.put("uid", userId);
				pushJson.put("msg", msgJson);
				pushJson.put("is_show_notify", false);
				pushJson.put("click_act", "");
				
				String result = HttpClientSender.post(GlobalObject.HTTP_CLIENT_CHATSERVICE, GlobalConfig.CHAT_SERVICE_URL, pushJson.toString());
				GlobalObject.INFO_LOG.info("push task notify:" + pushJson.toString() + " result:" + result);
			}
			catch(Exception e)
			{
				GlobalObject.ERROR_LOG.error("at TaskExecutor.pushNotify throw an error.", e);
			}
		}
	}
}