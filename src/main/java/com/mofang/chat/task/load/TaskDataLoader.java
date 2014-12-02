package com.mofang.chat.task.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.service.UserTaskService;
import com.mofang.chat.task.service.impl.UserTaskServiceImpl;
import com.mofang.framework.util.StringUtil;

public class TaskDataLoader
{
	private UserTaskService userTaskService = UserTaskServiceImpl.getInstance();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void handle(String filepath, int event)
	{
		File file = new File(filepath);
        BufferedReader reader = null;
        try
        {
            	reader = new BufferedReader(new FileReader(file));
            	String line = null;
            	long userId = 0L;
            int index = 1;
            	while ((line = reader.readLine()) != null)
            {
            		if(!StringUtil.isLong(line))
            			continue;
            		
            		if(index <= 100000)
            		{
            			index++;
            			continue;
            		}
            		userId = Long.parseLong(line);
            		create(userId, event);
            		String message = "[" + format.format(new Date()) +  "]  " + (index++) + " create task completed.";
	   			System.out.println(message);
	   			GlobalObject.INFO_LOG.info(message);
            }
        } 
        catch (Exception e)
        {
        		GlobalObject.ERROR_LOG.error("at TaskDataLoader.handle throw an error.", e);
            e.printStackTrace();
        } 
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (Exception e1) 
                {}
            }
        }
	}
	
	private void create(long userId, int event)
	{
		try
		{
			userTaskService.execute(userId, event);
		}
		catch(Exception e)
		{
			GlobalObject.ERROR_LOG.error("at TaskDataLoader.create throw an error. userId:" + userId + ", event:" + event, e);
		}
	}
}
