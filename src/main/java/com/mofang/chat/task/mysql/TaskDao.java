package com.mofang.chat.task.mysql;

import java.util.List;

import com.mofang.chat.task.model.Task;

/**
 * 
 * @author zhaodx
 *
 */
public interface TaskDao
{
	public void add(Task model) throws Exception;
	
	public void update(Task model) throws Exception;
	
	public void delete(Integer taskId) throws Exception;
	
	public Task getInfo(Integer taskId) throws Exception;
	
	public List<Task> getListByEvent(int event) throws Exception;
	
	public Task getTaskByMedalEvent(int medalEvent) throws Exception;
}