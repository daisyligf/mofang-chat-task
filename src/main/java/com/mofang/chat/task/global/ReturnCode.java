package com.mofang.chat.task.global;

public class ReturnCode
{
	/**
	 * 操作成功
	 */
	public final static int SUCCESS = 0;
	
	/**
	 * 无效参数
	 */
	public final static int CLIENT_REQUEST_DATA_IS_INVALID = 400;
	
	/**
	 * 请求参数格式不正确
	 */
	public final static int CLIENT_REQUEST_PARAMETER_FORMAT_ERROR = 401;
	
	/**
	 * 请求缺少必要参数
	 */
	public final static int CLIENT_REQUEST_LOST_NECESSARY_PARAMETER = 402;
	
	/**
	 * 服务器错误
	 */
	public final static int SERVER_ERROR = 500;
	
	/**
	 * 用户等级不足（没有达到任务限制等级）
	 */
	public final static int USER_LEVEL_INSUFFICIENT = 910;
	
	/**
	 * 任务不存在
	 */
	public final static int TASK_NOT_EXISTS = 911;
}