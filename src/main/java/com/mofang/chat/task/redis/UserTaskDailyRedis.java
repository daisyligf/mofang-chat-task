package com.mofang.chat.task.redis;

import java.util.Map;


/**
 * 
 * @author zhaodx
 *
 */
public interface UserTaskDailyRedis
{
	public long incr(long userId, int taskId, int taskDate) throws Exception;
	
	public long getCompletedCount(long userId, int taskId, int taskDate) throws Exception;
	
	public Map<Integer, Long> getCompletedCountMap(long userId, int taskDate) throws Exception;
}