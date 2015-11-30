package com.mofang.chat.task.init.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.chat.task.global.ReturnMessage;
import com.mofang.chat.task.init.AbstractInitializer;



/**
 * 
 * @author kehz
 *
 */
public class ReturnMessageInitializer extends AbstractInitializer
{
	@Override
	public void load() throws Exception
	{
		Properties configurations = loadConfig(GlobalConfig.RETURN_MESSAGE_CONFIG_PATH);
		ReturnMessage.SUCCESS = configurations.getProperty("SUCCESS");
		ReturnMessage.CLIENT_REQUEST_DATA_IS_INVALID = configurations.getProperty("CLIENT_REQUEST_DATA_IS_INVALID");
		ReturnMessage.CLIENT_REQUEST_PARAMETER_FORMAT_ERROR = configurations.getProperty("CLIENT_REQUEST_PARAMETER_FORMAT_ERROR");
		ReturnMessage.CLIENT_REQUEST_LOST_NECESSARY_PARAMETER = configurations.getProperty("CLIENT_REQUEST_LOST_NECESSARY_PARAMETER");
		ReturnMessage.SERVER_ERROR = configurations.getProperty("SERVER_ERROR");
		ReturnMessage.USER_LEVEL_INSUFFICIENT = configurations.getProperty("USER_LEVEL_INSUFFICIENT");
		ReturnMessage.TASK_NOT_EXISTS = configurations.getProperty("TASK_NOT_EXISTS");
	}
	
	private static Properties loadConfig(String configPath) throws Exception
	{
		Properties configurations = new Properties();
        File file = new File(configPath);
        try
        {
	        	configurations.load(new FileInputStream(file));
	        	return configurations;
        }
        catch(Exception e)
        {
        		throw e;
        }
	}
}