package com.mofang.chat.task.mysql.impl;

import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.model.UserTask;
import com.mofang.chat.task.mysql.UserTaskDao;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;

/**
 * 
 * @author zhaodx
 *
 */
public class UserTaskDaoImpl extends AbstractMysqlSupport<UserTask> implements UserTaskDao
{
	private final static UserTaskDaoImpl DAO = new UserTaskDaoImpl();
	
	private UserTaskDaoImpl()
	{
		try
		{
			super.setMysqlPool(GlobalObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static UserTaskDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public void add(UserTask model) throws Exception
	{
		super.insert(model);
	}

	@Override
	public void update(UserTask model) throws Exception
	{
		StringBuilder strSql = new StringBuilder();
		strSql.append("update user_task set ");
		strSql.append("completed_count=" + model.getCompletedCount() + ",");
		strSql.append("is_completed=" + model.getIsCompleted() + " ");
		strSql.append("where user_id=" + model.getUserId() + " ");
		strSql.append("and task_id=" + model.getTaskId() + " ");
		strSql.append("and task_date=" + model.getTaskDate());
		super.execute(strSql.toString());
	}
}