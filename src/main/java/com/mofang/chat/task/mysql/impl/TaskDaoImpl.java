package com.mofang.chat.task.mysql.impl;

import java.util.List;

import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.model.Task;
import com.mofang.chat.task.mysql.TaskDao;
import com.mofang.framework.data.mysql.AbstractMysqlSupport;
import com.mofang.framework.data.mysql.core.criterion.operand.EqualOperand;
import com.mofang.framework.data.mysql.core.criterion.operand.Operand;
import com.mofang.framework.data.mysql.core.criterion.operand.WhereOperand;

/**
 * 
 * @author zhaodx
 *
 */
public class TaskDaoImpl extends AbstractMysqlSupport<Task> implements TaskDao
{
	private final static TaskDaoImpl DAO = new TaskDaoImpl();
	
	private TaskDaoImpl()
	{
		try
		{
			super.setMysqlPool(GlobalObject.MYSQL_CONNECTION_POOL);
		}
		catch(Exception e)
		{}
	}
	
	public static TaskDaoImpl getInstance()
	{
		return DAO;
	}

	@Override
	public void add(Task model) throws Exception
	{
		super.insert(model);
	}

	@Override
	public void update(Task model) throws Exception
	{
		super.updateByPrimaryKey(model);
	}

	@Override
	public void delete(Integer taskId) throws Exception
	{
		super.deleteByPrimaryKey(taskId);
	}

	@Override
	public Task getInfo(Integer taskId) throws Exception
	{
		return super.getByPrimaryKey(taskId);
	}

	@Override
	public List<Task> getListByEvent(int event) throws Exception
	{
		Operand where = new WhereOperand();
		Operand equal = new EqualOperand("event", event);
		where.append(equal);
		return super.getList(where);
	}
}