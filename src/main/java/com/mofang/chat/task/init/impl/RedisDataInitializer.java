package com.mofang.chat.task.init.impl;

import com.mofang.chat.task.init.AbstractInitializer;
import com.mofang.chat.task.redis.TaskRedis;
import com.mofang.chat.task.redis.impl.TaskRedisImpl;

/**
 * 
 * @author zhaodx
 *
 */
public class RedisDataInitializer extends AbstractInitializer
{
	private TaskRedis taskRedis = TaskRedisImpl.getInstance();
	
	@Override
	public void load() throws Exception
	{
		taskRedis.initMaxId();
	}
}