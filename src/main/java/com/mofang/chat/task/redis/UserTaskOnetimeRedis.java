package com.mofang.chat.task.redis;

import java.util.Map;

/**
 * 
 * @author zhaodx
 *
 */
public interface UserTaskOnetimeRedis
{
	public long incr(long userId, int taskId) throws Exception;
	
	public long getCompletedCount(long userId, int taskId) throws Exception;
	
	public Map<Integer, Long> getCompletedCountMap(long userId) throws Exception;
}