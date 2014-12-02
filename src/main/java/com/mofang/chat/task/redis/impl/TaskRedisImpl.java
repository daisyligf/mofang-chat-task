package com.mofang.chat.task.redis.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.mofang.chat.task.global.GlobalConfig;
import com.mofang.chat.task.global.GlobalObject;
import com.mofang.chat.task.global.RedisKey;
import com.mofang.chat.task.model.Task;
import com.mofang.chat.task.redis.TaskRedis;
import com.mofang.framework.data.redis.RedisWorker;
import com.mofang.framework.data.redis.workers.GetWorker;
import com.mofang.framework.data.redis.workers.IncrWorker;
import com.mofang.framework.data.redis.workers.SetWorker;
import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class TaskRedisImpl implements TaskRedis
{
	private final static TaskRedisImpl REDIS = new TaskRedisImpl();
	private final static int LIMIT_RANGE = 50;
	
	private TaskRedisImpl()
	{}
	
	public static TaskRedisImpl getInstance()
	{
		return REDIS;
	}
	
	@Override
	public boolean initMaxId() throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception
			{
				String key = RedisKey.TASK_ID_INCREMENT_KEY;
				boolean exists = jedis.exists(key);
				if(!exists)
				{
					///初始化任务ID起始值
					jedis.set(key, String.valueOf(GlobalConfig.TASK_ID_START));
				}
				return true;
			}
		};
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public Long getMaxId() throws Exception
	{
		RedisWorker<Long> worker = new IncrWorker(RedisKey.TASK_ID_INCREMENT_KEY);
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean save(final Task model) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String infoKey = RedisKey.TASK_INFO_KEY_PREFIX + model.getTaskId();
				String listKey = RedisKey.TASK_LIST_KEY;
				
				///将任务信息添加到redis中
				JSONObject json = model.toJson();
				jedis.set(infoKey, json.toString());
				
				///将任务ID添加到任务列表中
				jedis.zadd(listKey, model.getDisplayOrder(), String.valueOf(model.getTaskId()));
				return true;
			}
		};
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean update(Task model) throws Exception
	{
		String infoKey = RedisKey.TASK_INFO_KEY_PREFIX + model.getTaskId();
		///将任务信息添加到redis中
		JSONObject json = model.toJson();
		RedisWorker<Boolean> worker = new SetWorker(infoKey, json.toString());
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public boolean delete(final int taskId) throws Exception
	{
		RedisWorker<Boolean> worker = new RedisWorker<Boolean>()
		{
			@Override
			public Boolean execute(Jedis jedis) throws Exception 
			{
				String infoKey = RedisKey.TASK_INFO_KEY_PREFIX + taskId;
				String listKey = RedisKey.TASK_LIST_KEY;
				
				///将任务信息从redis中删除
				jedis.del(infoKey);

				///将任务ID从任务列表中删除
				jedis.zrem(listKey, String.valueOf(taskId));
				return true;
			}
		};
		return GlobalObject.REDIS_MASTER_EXECUTOR.execute(worker);
	}

	@Override
	public Task getInfo(int taskId) throws Exception
	{
		String infoKey = RedisKey.TASK_INFO_KEY_PREFIX + taskId;
		RedisWorker<String> worker = new GetWorker(infoKey);
		String value = GlobalObject.REDIS_SLAVE_EXECUTOR.execute(worker);
		if(StringUtil.isNullOrEmpty(value))
			return null;
		
		JSONObject json = new JSONObject(value);
		return Task.buildByJson(json);
	}

	@Override
	public List<Task> getList() throws Exception
	{
		RedisWorker<List<Task>> worker = new RedisWorker<List<Task>>()
		{
			@Override
			public List<Task> execute(Jedis jedis) throws Exception 
			{
				String listKey = RedisKey.TASK_LIST_KEY;
				Set<String> taskSet = jedis.zrange(listKey, 0, LIMIT_RANGE);
				if(null == taskSet)
					return null;
				
				List<Task> list = new ArrayList<Task>();
				Task task = null;
				for(String taskId : taskSet)
				{
					task = getInfo(Integer.parseInt(taskId));
					if(null != task)
						list.add(task);
				}
				return list;
			}
		};
		return GlobalObject.REDIS_SLAVE_EXECUTOR.execute(worker);
	}
}