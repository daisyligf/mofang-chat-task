package com.mofang.chat.task.mysql;

import com.mofang.chat.task.model.UserTask;

/**
 * 
 * @author zhaodx
 *
 */
public interface UserTaskDao
{
	public void add(UserTask model) throws Exception; 
	
	public void update(UserTask model) throws Exception;
}