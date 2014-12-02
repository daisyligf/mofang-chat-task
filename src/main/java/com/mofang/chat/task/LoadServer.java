package com.mofang.chat.task;

import com.mofang.chat.task.init.Initializer;
import com.mofang.chat.task.init.impl.MainInitializer;
import com.mofang.chat.task.load.TaskDataLoader;

public class LoadServer {

	public static void main(String[] args) 
	{
		//String configpath = "/Users/milo/document/workspace/mofang.chat.task/src/main/resources/config.ini";
		
		if(args.length <= 0)
		{
			System.out.println("usage:java -server -Xms1024m -Xmx1024m -jar mofang-chat-task.jar configpath");
			System.exit(1);
		}
		String configpath = args[0];
		
		try
		{
			///服务器初始化
			System.out.println("prepare to initializing config......");
			Initializer initializer = new MainInitializer(configpath);
			initializer.init();
			System.out.println("initialize config completed!");
			
			/// 101: 注册    105: 绑定手机    107: 加入公会
			int event = Integer.parseInt(args[2]);
			String filePath = args[1];
			
			//int event = 107;
			//String filePath = "/Users/milo/document/workspace/mofang.chat.task/guild_userlist_new.txt";
			TaskDataLoader loader = new TaskDataLoader();
			loader.handle(filePath, event);
			System.out.println("task execute completed!");
		}
		catch (Exception e)
		{
			System.out.println("task server start error. message:");
			e.printStackTrace();
		}
	}
}
