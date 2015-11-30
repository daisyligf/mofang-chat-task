package com.mofang.chat.task.init.impl;

import java.io.IOException;

import com.mofang.chat.task.init.AbstractInitializer;
import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.framework.util.IniParser;

/**
 * 
 * @author zhaodx
 *
 */
public class GlobalConfigInitializer extends AbstractInitializer
{
	private String configPath;
	
	public GlobalConfigInitializer(String configPath)
	{
		this.configPath = configPath;
	}
	
	@Override
	public void load() throws IOException 
	{
		IniParser config = new IniParser(configPath);
		GlobalConfig.SERVER_PORT = config.getInt("common", "server_port");
		GlobalConfig.CONN_TIMEOUT = config.getInt("common", "conn_timeout");
		GlobalConfig.READ_TIMEOUT = config.getInt("common", "read_timeout");
		GlobalConfig.TASK_ID_START = config.getInt("common", "task_id_start");
		
		GlobalConfig.SCAN_PACKAGE_PATH = config.get("conf", "scan_package_path");
		GlobalConfig.MYSQL_CONFIG_PATH = config.get("conf", "mysql_config_path");
		GlobalConfig.REDIS_MASTER_CONFIG_PATH = config.get("conf", "redis_master_config_path");
		GlobalConfig.REDIS_SLAVE_CONFIG_PATH = config.get("conf", "redis_slave_config_path");
		GlobalConfig.LOG4J_CONFIG_PATH = config.get("conf", "log4j_config_path");
		GlobalConfig.HTTP_CLIENT_GROWUP_CONFIG_PATH = config.get("conf", "http_client_growup_config_path");
		GlobalConfig.HTTP_CLIENT_CHATSERVICE_CONFIG_PATH = config.get("conf", "http_client_chatservice_config_path");
		GlobalConfig.HTTP_CLIENT_MEDAL_CONFIG_PATH = config.get("conf", "http_client_medal_config_path");
		GlobalConfig.RETURN_MESSAGE_CONFIG_PATH = config.get("conf", "return_messsage_config_path");
		
		GlobalConfig.CHAT_SERVICE_URL = config.get("api", "chat_service_url");
		GlobalConfig.GROWUP_USER_UPDATE_URL = config.get("api", "growup_user_update_url");
		GlobalConfig.GROWUP_USER_INFO_URL = config.get("api", "growup_user_info_url");
		GlobalConfig.MEDAL_COMPLETED_URL = config.get("api", "medal_completed_url");
	}
}