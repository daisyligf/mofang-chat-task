package com.mofang.chat.task.init.impl;

import com.mofang.chat.task.init.AbstractInitializer;
import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.chat.task.global.GlobalObject;

/**
 * 
 * @author zhaodx
 *
 */
public class GlobalObjectInitializer extends AbstractInitializer
{
	@Override
	public void load() throws Exception
	{
		GlobalObject.initRedisMaster(GlobalConfig.REDIS_MASTER_CONFIG_PATH);
		GlobalObject.initRedisSlave(GlobalConfig.REDIS_SLAVE_CONFIG_PATH);
		GlobalObject.initMysql(GlobalConfig.MYSQL_CONFIG_PATH);
		GlobalObject.initGrowupHttpClient(GlobalConfig.HTTP_CLIENT_GROWUP_CONFIG_PATH);
		GlobalObject.initChatServiceHttpClient(GlobalConfig.HTTP_CLIENT_CHATSERVICE_CONFIG_PATH);
		GlobalObject.initMedalHttpClient(GlobalConfig.HTTP_CLIENT_MEDAL_CONFIG_PATH);
	}
}