package com.mofang.chat.task.model;

import java.util.Date;

import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.TableName;

/**
 * 
 * @author zhaodx
 *
 */
@TableName(name="user_task")
public class UserTask
{
	@ColumnName(name="user_id")
	private Long userId;
	@ColumnName(name="task_id")
	private Integer taskId;
	@ColumnName(name="task_date")
	private Integer taskDate;
	@ColumnName(name="completed_count")
	private Integer completedCount;
	@ColumnName(name="is_completed")
	private Boolean isCompleted;
	@ColumnName(name="completed_time")
	private Date completedTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Integer taskDate) {
		this.taskDate = taskDate;
	}
	public Integer getCompletedCount() {
		return completedCount;
	}
	public void setCompletedCount(Integer completedCount) {
		this.completedCount = completedCount;
	}
	public Boolean getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public Date getCompletedTime() {
		return completedTime;
	}
	public void setCompletedTime(Date completedTime) {
		this.completedTime = completedTime;
	}
}