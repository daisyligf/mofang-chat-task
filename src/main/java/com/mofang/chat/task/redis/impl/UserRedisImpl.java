package com.mofang.chat.task.redis.impl;

import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.global.RedisKey;
import com.mofang.chat.task.redis.UserRedis;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.GetWorker;
import com.mofang.framework.data.redis.workers.SetWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class UserRedisImpl implements UserRedis
{
	private final static UserRedisImpl REDIS = new UserRedisImpl();
	
	private UserRedisImpl()
	{}
	
	public static UserRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public boolean setInfo(long userId, int level) throws Exception
	{
		String key = RedisKey.USER_KEY_PREFIX + userId;
		RedisWorker<Boolean> worker = new SetWorker(key, String.valueOf(level));
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public String getInfo(long userId) throws Exception
	{
		String key = RedisKey.USER_KEY_PREFIX + userId;
		RedisWorker<String> worker = new GetWorker(key);
		return GlobalObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}