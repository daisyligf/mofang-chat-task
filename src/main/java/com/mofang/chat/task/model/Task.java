package com.mofang.chat.task.model;

import java.util.Date;

import org.json.JSONObject;

import com.mofang.chat.task.global.common.TaskType;
import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.PrimaryKey;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="task")
public class Task
{
	@PrimaryKey
	@ColumnName(name="task_id")
	private Integer taskId;
	@ColumnName(name="task_name")
	private String taskName;
	@ColumnName(name="type")
	private Integer type;
	@ColumnName(name="event")
	private Integer event;
	@ColumnName(name="description")
	private String description;
	@ColumnName(name="limit_level")
	private Integer limitLevel;
	@ColumnName(name="limit_times")
	private Integer limitTimes;
	@ColumnName(name="event_param")
	private Integer eventParam;
	@ColumnName(name="display_order")
	private Integer displayOrder;
	@ColumnName(name="reward_coin")
	private Float rewardCoin;
	@ColumnName(name="reward_diamond")
	private Float rewardDiamond;
	@ColumnName(name="reward_exp")
	private Integer rewardExp;
	@ColumnName(name="create_time")
	private Date createTime;
	
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getEvent() {
		return event;
	}
	public void setEvent(Integer event) {
		this.event = event;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLimitLevel() {
		return limitLevel;
	}
	public void setLimitLevel(Integer limitLevel) {
		this.limitLevel = limitLevel;
	}
	public Integer getLimitTimes() {
		return limitTimes;
	}
	public void setLimitTimes(Integer limitTimes) {
		this.limitTimes = limitTimes;
	}
	public Integer getEventParam() {
		return eventParam;
	}
	public void setEventParam(Integer eventParam) {
		this.eventParam = eventParam;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Float getRewardCoin() {
		return rewardCoin;
	}
	public void setRewardCoin(Float rewardCoin) {
		this.rewardCoin = rewardCoin;
	}
	public Float getRewardDiamond() {
		return rewardDiamond;
	}
	public void setRewardDiamond(Float rewardDiamond) {
		this.rewardDiamond = rewardDiamond;
	}
	public Integer getRewardExp() {
		return rewardExp;
	}
	public void setRewardExp(Integer rewardExp) {
		this.rewardExp = rewardExp;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("id", taskId);
			json.put("name", taskName == null ? "" : taskName);
			json.put("type", type);
			json.put("event", event);
			json.put("description", description == null ? "" : description);
			json.put("limitLevel", limitLevel);
			json.put("limitTimes", limitTimes);
			json.put("eventParam", eventParam);
			json.put("displayOrder", displayOrder);
			json.put("rewardCoin", rewardCoin);
			json.put("rewardDiamond", rewardDiamond);
			json.put("rewardExp", rewardExp);
			json.put("createTime", createTime == null ? System.currentTimeMillis() : createTime.getTime());
			return json;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Task buildByJson(JSONObject json)
	{
		Task model = new Task();
		try
		{
			model.setTaskId(json.optInt("id", 0));
			model.setTaskName(json.optString("name", ""));
			model.setType(json.optInt("type", TaskType.DAILY));
			model.setEvent(json.optInt("event", 0));
			model.setDescription(json.optString("description", ""));
			model.setLimitLevel(json.optInt("limitLevel", 1));
			model.setLimitTimes(json.optInt("limitTimes", 1));
			model.setEventParam(json.optInt("eventParam", 1));
			model.setDisplayOrder(json.optInt("displayOrder", 1));
			model.setRewardCoin(Float.parseFloat(json.optString("rewardCoin", "0")));
			model.setRewardDiamond(Float.parseFloat(json.optString("rewardDiamond", "0")));
			model.setRewardExp(json.optInt("rewardExp", 0));
			long time = json.optLong("createTime", System.currentTimeMillis());
			model.setCreateTime(new Date(time));
			return model;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}