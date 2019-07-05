package com.ld.mall.vo;

import com.ld.mall.pojo.User;

public class UserVo {
	private boolean isExit;
	private boolean passwordInfo;
	private boolean active;
	private User user;
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isExit() {
		return isExit;
	}
	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}
	public boolean isPasswordInfo() {
		return passwordInfo;
	}
	public void setPasswordInfo(boolean passwordInfo) {
		this.passwordInfo = passwordInfo;
	}
	
}
