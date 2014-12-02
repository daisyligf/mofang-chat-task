package com.mofang.chat.task.service.impl;

import org.json.JSONObject;

import com.mofang.chat.task.component.GrowupComponent;
import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.redis.UserRedis;
import com.mofang.chat.task.redis.impl.UserRedisImpl;
import com.mofang.chat.task.service.UserService;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class UserServiceImpl implements UserService
{
	private final static UserServiceImpl SERVICE = new UserServiceImpl();
	private UserRedis userRedis = UserRedisImpl.getInstance();
	
	private UserServiceImpl()
	{}
	
	public static UserServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public int getUserLevel(long userId)
	{
		try
		{
			String info = userRedis.getInfo(userId);
			if(StringUtil.isNullOrEmpty(info))
			{
				info = GrowupComponent.getUserInfo(userId);
				if(StringUtil.isNullOrEmpty(info))
					return 1;
				
				JSONObject json = new JSONObject(info);
				JSONObject data = json.optJSONObject("data");
				if(null == data)
					return 1;
				
				int level = data.optInt("level", 1);
				userRedis.setInfo(userId, level);
				return level;
			}
			else
			{
				int level = Integer.parseInt(info);
				AsyncUpdateWorker worker = new AsyncUpdateWorker(userId);
				GlobalObject.REDIS_ASYNC_UPDATE_EXECUTOR.execute(worker);
				return level;
			}
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("at UserServiceImpl.getUserLevel throw an error", e);
			return 1;
		}
	}
	
	class AsyncUpdateWorker implements Runnable
	{
		private long userId;
		
		public AsyncUpdateWorker(long userId)
		{
			this.userId = userId;
		}

		@Override
		public void run()
		{
			try
			{
				String result = GrowupComponent.getUserInfo(userId);
				if(StringUtil.isNullOrEmpty(result))
					return;
				
				JSONObject json = new JSONObject(result);
				JSONObject data = json.optJSONObject("data");
				if(null == data)
					return;
				
				int level = data.optInt("level", 1);
				userRedis.setInfo(userId, level);
			}
			catch(Exception e)
			{
				GlobalObject.ERROR_LOG.error("at AsyncUpdateWorker.run throw an error", e);
			}
		}
	}
}