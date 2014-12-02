package com.mofang.chat.task.service;

import org.json.JSONArray;

/**
 * 
 * @author zhaodx
 *
 */
public interface UserTaskService
{
	public boolean execute(long userId, int event) throws Exception;
	
	public JSONArray getTaskList(long userId) throws Exception;
}