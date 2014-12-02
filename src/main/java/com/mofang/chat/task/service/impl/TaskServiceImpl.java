package com.mofang.chat.task.service.impl;

import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.model.Task;
import com.mofang.chat.task.mysql.TaskDao;
import com.mofang.chat.task.mysql.impl.TaskDaoImpl;
import com.mofang.chat.task.redis.TaskRedis;
import com.mofang.chat.task.redis.impl.TaskRedisImpl;
import com.mofang.chat.task.service.TaskService;

/**
 * 
 * @author zhaodx
 *
 */
public class TaskServiceImpl implements TaskService
{
	private final static TaskServiceImpl SERVICE = new TaskServiceImpl();
	private TaskRedis taskRedis = TaskRedisImpl.getInstance();
	private TaskDao taskDao = TaskDaoImpl.getInstance();
	
	private TaskServiceImpl()
	{}
	
	public static TaskServiceImpl getInstance()
	{
		return SERVICE;
	}

	@Override
	public void add(Task model) throws Exception
	{
		try
		{
			taskRedis.save(model);
			taskDao.add(model);
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("at TaskServiceImpl.add throw an error.", e);
			throw e;
		}
	}

	@Override
	public void update(Task model) throws Exception
	{
		try
		{
			taskRedis.save(model);
			taskDao.update(model);
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("at TaskServiceImpl.update throw an error.", e);
			throw e;
		}
	}

	@Override
	public void delete(int taskId) throws Exception
	{
		try
		{
			taskRedis.delete(taskId);
			taskDao.delete(taskId);
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("at TaskServiceImpl.delete throw an error.", e);
			throw e;
		}
	}
}