package com.mofang.chat.task.controller.task;

import com.mofang.chat.task.controller.AbstractActionExecutor;
import com.mofang.chat.task.global.ResultValue;
import com.mofang.chat.task.logic.TaskLogic;
import com.mofang.chat.task.logic.impl.TaskLogicImpl;
import com.mofang.framework.web.server.annotation.Action;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author zhaodx
 *
 */
@Action(url="task/delete")
public class TaskDeleteAction extends AbstractActionExecutor
{
	private TaskLogic logic = TaskLogicImpl.getInstance();

	@Override
	protected ResultValue exec(HttpRequestContext context) throws Exception
	{
		return logic.delete(context);
	}

	protected boolean needCheckAtom()
	{
		return false;
	}
}