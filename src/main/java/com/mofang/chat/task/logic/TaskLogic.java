package com.mofang.chat.task.logic;

import com.mofang.chat.task.global.ResultValue;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;

/**
 * 
 * @author zhaodx
 *
 */
public interface TaskLogic
{
	public ResultValue add(HttpRequestContext context) throws Exception;
	
	public ResultValue update(HttpRequestContext context) throws Exception;
	
	public ResultValue delete(HttpRequestContext context) throws Exception;
}