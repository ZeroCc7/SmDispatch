package com.dispatchtask.vo;

public class Smdown {
	private int downid;// number(18) 待发序列号，唯一值，EC侧填写
	private String sm_serialphones;// varchar2(4000) 目标手机号码，填写格式：id:mobile；其中id为手机号码身份id，多号码之间以英文'',''逗号间隔，最多支持150个，同一记录多个号码必须是同属一个运营商的号码
	private String sm_content;// varchar2(30) N 短信消息的内容
	private int sm_ismgtype;// number(2) 运营商类型标识，0：移动；1：联通；2：电信
	private String sm_servicecode;//varchar2(21) 扩展号码，长号码
	private String sm_sendtime;// date y 发送起始时间（ec侧一般会赋1900-01-01，意味着立即下发）
	private String sm_batchno;// char(1) n 批次信息
	private String sm_servicename;// varchar2(200) Y 业务信息
	private String downtime;// number(8) y 创建时间
	private int sendlevel;// number(8) Y消息发送优先级别，0最低等级 .因此，单号码发送为1，多号码发送为0
	private int taskstatus;//number(2) 短信任务发送处理状态标志，0:未处理；1：已处理
	
	public int getDownid() {
		return downid;
	}
	public void setDownid(int downid) {
		this.downid = downid;
	}
	
	public String getSm_serialphones() {
		return sm_serialphones;
	}
	public void setSm_serialphones(String sm_serialphones) {
		this.sm_serialphones = sm_serialphones;
	}
	public String getSm_content() {
		return sm_content;
	}
	public void setSm_content(String sm_content) {
		this.sm_content = sm_content;
	}
	public int getSm_ismgtype() {
		return sm_ismgtype;
	}
	public void setSm_ismgtype(int sm_ismgtype) {
		this.sm_ismgtype = sm_ismgtype;
	}
	public String getSm_servicecode() {
		return sm_servicecode;
	}
	public void setSm_servicecode(String sm_servicecode) {
		this.sm_servicecode = sm_servicecode;
	}
	public String getSm_sendtime() {
		return sm_sendtime;
	}
	public void setSm_sendtime(String sm_sendtime) {
		this.sm_sendtime = sm_sendtime;
	}
	public String getSm_batchno() {
		return sm_batchno;
	}
	public void setSm_batchno(String sm_batchno) {
		this.sm_batchno = sm_batchno;
	}
	public String getSm_servicename() {
		return sm_servicename;
	}
	public void setSm_servicename(String sm_servicename) {
		this.sm_servicename = sm_servicename;
	}
	public String getDowntime() {
		return downtime;
	}
	public void setDowntime(String downtime) {
		this.downtime = downtime;
	}
	public int getSendlevel() {
		return sendlevel;
	}
	public void setSendlevel(int sendlevel) {
		this.sendlevel = sendlevel;
	}
	public int getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(int taskstatus) {
		this.taskstatus = taskstatus;
	}

	
}
