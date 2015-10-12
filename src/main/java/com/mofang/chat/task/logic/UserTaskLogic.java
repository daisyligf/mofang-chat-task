package com.mofang.chat.task.logic;

import com.mofang.chat.task.global.ResultValue;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author zhaodx
 *
 */
public interface UserTaskLogic
{
	public ResultValue execute(HttpRequestContext context) throws Exception;
	
	public ResultValue getTaskList(HttpRequestContext context) throws Exception;
	
	public ResultValue getTaskCompletedCount(HttpRequestContext context) throws Exception;
	
	public ResultValue share(HttpRequestContext context) throws Exception;
	
	public ResultValue active(HttpRequestContext context) throws Exception;
	
	public ResultValue startGame(HttpRequestContext context) throws Exception;
	
	public ResultValue playVideo(HttpRequestContext context) throws Exception;
	
	public ResultValue downloadGame(HttpRequestContext context) throws Exception;
	
	public ResultValue appstoreComment(HttpRequestContext context) throws Exception;
}