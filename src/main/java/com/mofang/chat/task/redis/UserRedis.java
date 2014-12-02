package com.mofang.chat.task.redis;

public interface UserRedis
{
	public boolean setInfo(long userId, int level) throws Exception;
	
	public String getInfo(long userId) throws Exception;
}