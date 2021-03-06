package com.mofang.chat.task.controller.task.user;

import com.mofang.chat.task.controller.AbstractActionExecutor;
import com.mofang.chat.task.global.ResultValue;
import com.mofang.chat.task.logic.UserTaskLogic;
import com.mofang.chat.task.logic.impl.UserTaskLogicImpl;
import com.mofang.framework.web.server.annotation.Action;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author zhaodx
 *
 */
@Action(url="task/list")
public class UserTaskListAction extends AbstractActionExecutor
{
	private UserTaskLogic logic = UserTaskLogicImpl.getInstance();

	@Override
	protected ResultValue exec(HttpRequestContext context) throws Exception
	{
		return logic.getTaskList(context);
	}
}