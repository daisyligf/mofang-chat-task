package com.mofang.chat.task.global;

/**
 * 
 * @author zhaodx
 *
 */
public class RedisKey
{
	/**
	 * 任务自增IDkey
	 * string结构: incr task_id
	 */
	public final static String TASK_ID_INCREMENT_KEY = "task_id";
	
	/**
	 * 任务信息key前缀
	 * string结构: set task_info_{taskid} {taskmodel}
	 */
	public final static String TASK_INFO_KEY_PREFIX = "task_info_";
	
	/**
	 * 任务列表key
	 * set结构: sadd task_list {taskid}
	 */
	public final static String TASK_LIST_KEY = "task_list";
	
	/**
	 * 用户日常任务列表key前缀
	 * hash结构: user_task_daily_list_{userid}_{taskdate}  {taskid} {usertaskdailymodel}
	 */
	public final static String USER_TASK_DAILY_LIST_KEY_PREFIX = "user_task_daily_list_";
	
	/**
	 * 用户一次性任务列表key前缀
	 * hash结构: user_task_onetime_list_{userid}_{taskdate}  {taskid} {usertaskonetimemodel}
	 */
	public final static String USER_TASK_ONETIME_LIST_KEY_PREFIX = "user_task_onetime_list_";
	
	/**
	 * 用户信息key前缀(目前只有等级字段)
	 * String结构：set user_{userid} {level}
	 */
	public final static String USER_KEY_PREFIX = "user_";
}