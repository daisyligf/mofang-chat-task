package com.mofang.chat.task.service;

import com.mofang.chat.task.model.Task;

/**
 * 
 * @author zhaodx
 *
 */
public interface TaskService
{
	public void add(Task model) throws Exception;
	
	public void update(Task model) throws Exception;
	
	public void delete(int taskId) throws Exception;
}