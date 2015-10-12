package com.mofang.chat.task.controller.task.user;

import com.mofang.chat.task.controller.AbstractActionExecutor;
import com.mofang.chat.task.global.ResultValue;
import com.mofang.chat.task.logic.UserTaskLogic;
import com.mofang.chat.task.logic.impl.UserTaskLogicImpl;
import com.mofang.framework.web.server.annotation.Action;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author milo
 *
 */
@Action(url="task/appstorecomment")
public class UserTaskAppstoreCommentAction extends AbstractActionExecutor
{
	private UserTaskLogic logic = UserTaskLogicImpl.getInstance();

	@Override
	protected ResultValue exec(HttpRequestContext context) throws Exception
	{
		return logic.appstoreComment(context);
	}
}