package com.mofang.chat.task.logic.impl;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mofang.chat.task.global.ResultValue;
import com.mofang.chat.task.global.ReturnCode;
import com.mofang.chat.task.global.ReturnMessage;
import com.mofang.chat.task.logic.UserTaskLogic;
import com.mofang.chat.task.service.UserTaskService;
import com.mofang.chat.task.service.impl.UserTaskServiceImpl;
import com.mofang.framework.util.StringUtil;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author zhaodx
 * event param:
 * 101: 注册
 * 102: 发帖
 * 103: 点赞
 * 104: 评论
 * 105: 绑定手机
 * 106: 加好友
 * 107: 加入公会
 * 108: 登录(活跃)
 * 109: 分享
 * 110: 收集赞(帖子被点赞)
 * 111: 启动游戏
 * 112: 上传头像
 * 113: 兑换达人(兑换物品)
 * 114: 万元户(领取魔币到达1W)
 * 115: 赌神(天天翻)
 * 116: 精华内容(帖子被加精) 
 * 117: 观看视频
 * 118: 下载游戏
 * 119: 回复32个主题
 * 120: appstore五星评论
 *
 */
public class UserTaskLogicImpl implements UserTaskLogic
{
	private final static UserTaskLogicImpl LOGIC = new UserTaskLogicImpl();
	private UserTaskService userTaskService = UserTaskServiceImpl.getInstance();
	
	private UserTaskLogicImpl()
	{}
	
	public static UserTaskLogicImpl getInstance()
	{
		return LOGIC;
	}

	@Override
	public ResultValue execute(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String postData = context.getPostData();
		if(StringUtil.isNullOrEmpty(postData))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			JSONObject json = new JSONObject(postData);
			long userId = json.optLong("uid", 0L);
			if(0L == userId)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				return result;
			}
			int event = json.optInt("event", 0);
			if(0 == event)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				result.setMessage(ReturnMessage.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				return result;
			}
			
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.execute throw an error.", e);
		}
	}

	@Override
	public ResultValue getTaskList(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			JSONArray data = userTaskService.getTaskList(userId);
			if(null == data)
				data = new JSONArray();
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			result.setData(data);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.getTaskList throw an error.", e);
		}
	}

	@Override
	public ResultValue getTaskCompletedCount(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		String strMedalEvent = context.getParameters("medal_event");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		if(!StringUtil.isInteger(strMedalEvent))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int medalEvent = Integer.parseInt(strMedalEvent);
			long completedCount = userTaskService.getTaskCompletedCount(userId, medalEvent);
			JSONObject data = new JSONObject();
			data.put("count", completedCount);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			result.setData(data);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.getTaskCompletedCount throw an error.", e);
		}
	}

	@Override
	public ResultValue share(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int event = 109;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.share throw an error.", e);
		}
	}

	@Override
	public ResultValue active(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int event = 108;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.activity throw an error.", e);
		}
	}

	@Override
	public ResultValue startGame(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int event = 111;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.startGame throw an error.", e);
		}
	}
	
	@Override
	public ResultValue playVideo(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int event = 117;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.playVideo throw an error.", e);
		}
	}
	
	@Override
	public ResultValue downloadGame(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int event = 118;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.downloadGame throw an error.", e);
		}
	}

	@Override
	public ResultValue appstoreComment(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String strUserId = context.getParameters("uid");
		if(!StringUtil.isLong(strUserId))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage(ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID);
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(strUserId);
			int event = 120;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage(ReturnMessage.SUCCESS);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.appstoreComment throw an error.", e);
		}
	}
}