package com.mofang.chat.task.logic.impl;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.task.global.ReturnCode;
import com.mofang.chat.task.global.ResultValue;
import com.mofang.chat.task.global.ReturnMessage;
import com.mofang.chat.task.logic.TaskLogic;
import com.mofang.chat.task.model.Task;
import com.mofang.chat.task.redis.TaskRedis;
import com.mofang.chat.task.redis.impl.TaskRedisImpl;
import com.mofang.chat.task.service.TaskService;
import com.mofang.chat.task.service.impl.TaskServiceImpl;
import com.mofang.framework.util.StringUtil;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author zhaodx
 *
 */
public class TaskLogicImpl implements TaskLogic
{
	private final static TaskLogicImpl LOGIC = new TaskLogicImpl();
	private TaskService taskService = TaskServiceImpl.getInstance();
	private TaskRedis taskRedis = TaskRedisImpl.getInstance();
	
	private TaskLogicImpl()
	{}
	
	public static TaskLogicImpl getInstance()
	{
		return LOGIC;
	}

	@Override
	public ResultValue add(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String postData = context.getPostData();
		if(StringUtil.isNullOrEmpty(postData))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			JSONObject json = new JSONObject(postData);
			String taskName = json.optString("name", "");
			if(StringUtil.isNullOrEmpty(taskName))
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			int type = json.optInt("type", 0);
			if(0 == type)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			int event = json.optInt("event", 0);
			if(0 == event)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			///0代表不限制等级
			int limitLevel = json.optInt("limit_level", 0);
			int limitTimes = json.optInt("limit_times", 0);
			if(0 == limitTimes)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			int eventParam = json.optInt("event_param", 0);
			if(0 == eventParam)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			int displayOrder = json.optInt("display_order", 1);
			String description = json.optString("description", "");
			Double rewardCoin = json.optDouble("reward_coin", 0.0);
			Double rewardDiamond = json.optDouble("reward_diamond", 0.0);
			int rewardExp = json.optInt("reward_exp", 0);
			int medalEvent = json.optInt("medal_event", 0);
			
			Task taskInfo = new Task();
			int taskId = taskRedis.getMaxId().intValue();
			taskInfo.setTaskId(taskId);
			taskInfo.setTaskName(taskName);
			taskInfo.setType(type);
			taskInfo.setEvent(event);
			taskInfo.setDescription(description);
			taskInfo.setLimitLevel(limitLevel);
			taskInfo.setLimitTimes(limitTimes);
			taskInfo.setEventParam(eventParam);
			taskInfo.setDisplayOrder(displayOrder);
			taskInfo.setRewardCoin(rewardCoin.floatValue());
			taskInfo.setRewardDiamond(rewardDiamond.floatValue());
			taskInfo.setRewardExp(rewardExp);
			taskInfo.setMedalEvent(medalEvent);
			taskInfo.setCreateTime(new Date());
			
			///保存任务信息
			taskService.add(taskInfo);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at TaskLogicImpl.add throw an error.", e);
		}
	}

	@Override
	public ResultValue update(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String postData = context.getPostData();
		if(StringUtil.isNullOrEmpty(postData))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			JSONObject json = new JSONObject(postData);
			int taskId = json.optInt("task_id", 0);
			if(0 == taskId)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				return result;
			}
			String taskName = json.optString("name", "");
			if(StringUtil.isNullOrEmpty(taskName))
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			///等级0代表不限制等级
			int limitLevel = json.optInt("limit_level", 0);
			
			int limitTimes = json.optInt("limit_times", 0);
			if(0 == limitTimes)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			int eventParam = json.optInt("event_param", 0);
			if(0 == eventParam)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
				return result;
			}
			String description = json.optString("description", "");
			Double rewardCoin = json.optDouble("reward_coin", 0.0);
			Double rewardDiamond = json.optDouble("reward_diamond", 0.0);
			int rewardExp = json.optInt("reward_exp", 0);
			int medalEvent = json.optInt("medal_event", 0);
			
			Task taskInfo = taskRedis.getInfo(taskId);
			if(null == taskInfo)
			{
				result.setCode(ReturnCode.TASK_NOT_EXISTS);
				result.setMessage(ReturnMessage.TASK_NOT_EXISTS);
				return result;
			}
			
			taskInfo.setTaskId(taskId);
			taskInfo.setTaskName(taskName);
			taskInfo.setDescription(description);
			taskInfo.setLimitLevel(limitLevel);
			taskInfo.setLimitTimes(limitTimes);
			taskInfo.setEventParam(eventParam);
			taskInfo.setRewardCoin(rewardCoin.floatValue());
			taskInfo.setRewardDiamond(rewardDiamond.floatValue());
			taskInfo.setRewardExp(rewardExp);
			taskInfo.setMedalEvent(medalEvent);
			
			///保存任务信息
			taskService.update(taskInfo);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at TaskLogicImpl.update throw an error.", e);
		}
	}

	@Override
	public ResultValue delete(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String postData = context.getPostData();
		if(StringUtil.isNullOrEmpty(postData))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			JSONObject json = new JSONObject(postData);
			int taskId = json.optInt("task_id", 0);
			if(0 == taskId)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				return result;
			}
			///删除任务信息
			taskService.delete(taskId);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at TaskLogicImpl.delete throw an error.", e);
		}
	}
}