package com.mofang.chat.task.logic.impl;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mofang.chat.task.global.ResultValue;
import com.mofang.chat.task.global.ReturnCode;
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
			return result;
		}
		
		try
		{
			JSONObject json = new JSONObject(postData);
			long userId = json.optLong("uid", 0L);
			if(0L == userId)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				result.setMessage("uid不能为空");
				return result;
			}
			int event = json.optInt("event", 0);
			if(0 == event)
			{
				result.setCode(ReturnCode.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER);
				result.setMessage("event不能为空");
				return result;
			}
			
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage("OK");
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
		String uidString = context.getParameters("uid");
		if(!StringUtil.isLong(uidString))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage("参数无效");
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(uidString);
			JSONArray data = userTaskService.getTaskList(userId);
			if(null == data)
				data = new JSONArray();
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage("OK");
			result.setData(data);
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.getTaskList throw an error.", e);
		}
	}

	@Override
	public ResultValue share(HttpRequestContext context) throws Exception
	{
		ResultValue result = new ResultValue();
		String uidString = context.getParameters("uid");
		if(!StringUtil.isLong(uidString))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage("参数无效");
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(uidString);
			int event = 109;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage("OK");
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
		String uidString = context.getParameters("uid");
		if(!StringUtil.isLong(uidString))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage("参数无效");
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(uidString);
			int event = 108;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage("OK");
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
		String uidString = context.getParameters("uid");
		if(!StringUtil.isLong(uidString))
		{
			result.setCode(ReturnCode.CLIENT_REQUEST_DATA_IS_INVALID);
			result.setMessage("参数无效");
			return result;
		}
		
		try
		{
			long userId = Long.parseLong(uidString);
			int event = 111;
			///执行任务
			userTaskService.execute(userId, event);
			
			///返回结果
			result.setCode(ReturnCode.SUCCESS);
			result.setMessage("OK");
			return result;
		}
		catch(Exception e)
		{
			throw new Exception("at UserTaskLogicImpl.startGame throw an error.", e);
		}
	}
}