package com.mofang.chat.task.redis;

import java.util.List;

import com.mofang.chat.task.model.Task;

/**
 * 
 * @author zhaodx
 *
 */
public interface TaskRedis
{
	public boolean initMaxId() throws Exception;
	
	public Long getMaxId() throws Exception;
	
	public boolean save(Task model) throws Exception;
	
	public boolean update(Task model) throws Exception;
	
	public boolean delete(int taskId) throws Exception;
	
	public Task getInfo(int taskId) throws Exception;
	
	public List<Task> getList() throws Exception;
}