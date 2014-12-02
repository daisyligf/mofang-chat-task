package com.mofang.chat.task.redis.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.global.RedisKey;
import com.mofang.chat.task.redis.UserTaskDailyRedis;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class UserTaskDailyRedisImpl implements UserTaskDailyRedis
{
	private final static UserTaskDailyRedisImpl REDIS = new UserTaskDailyRedisImpl();
	private final static int KEY_EXPIRE_SECONDS = 24 * 60 * 60;
	
	private UserTaskDailyRedisImpl()
	{}
	
	public static UserTaskDailyRedisImpl getInstance()
	{
		return REDIS;
	}

	@Override
	public long incr(final long userId, final int taskId, final int taskDate) throws Exception
	{
		RedisWorker<Long> worker = new RedisWorker<Long>()
		{
			@Override
			public Long execute(Jedis jedis) throws Exception 
			{
				String key = RedisKey.USER_TASK_DAILY_LIST_KEY_PREFIX + userId + "_" + taskDate;
				Long count = jedis.hincrBy(key, String.valueOf(taskId), 1);
				jedis.expire(key, KEY_EXPIRE_SECONDS);
				return count;
			}
		};
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public long getCompletedCount(final long userId, final int taskId, final int taskDate) throws Exception
	{
		RedisWorker<Long> worker = new RedisWorker<Long>()
		{
			@Override
			public Long execute(Jedis jedis) throws Exception 
			{
				String key = RedisKey.USER_TASK_DAILY_LIST_KEY_PREFIX + userId + "_" + taskDate;
				String value = jedis.hget(key, String.valueOf(taskId));
				if(!StringUtil.isLong(value))
					return 0L;
				return Long.parseLong(value);
			}
		};
		return GlobalObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}

	@Override
	public Map<Integer, Long> getCompletedCountMap(final long userId, final int taskDate) throws Exception
	{
		RedisWorker<Map<Integer, Long>> worker = new RedisWorker<Map<Integer, Long>>()
		{
			@Override
			public Map<Integer, Long> execute(Jedis jedis) throws Exception 
			{
				String key = RedisKey.USER_TASK_DAILY_LIST_KEY_PREFIX + userId + "_" + taskDate;
				Map<String, String> map = jedis.hgetAll(key);
				if(null == map)
					return null;
				
				Map<Integer, Long> completedMap = new HashMap<Integer, Long>();
				Iterator<String> iterator = map.keySet().iterator();
				String taskId = null;
				String value = null;
				while(iterator.hasNext())
				{
					taskId = iterator.next();
					value = map.get(taskId);
					completedMap.put(Integer.parseInt(taskId), Long.parseLong(value));
				}
				return completedMap;
			}
		};
		return GlobalObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}