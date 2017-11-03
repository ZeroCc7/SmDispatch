package com.dispatchtask.vo;

public class ReportVo {
	private String report_status;
	private String msgid;
	private String mobile;
	private String report;
	private String recv_time;
	private String uid;
	
	public String getReport_status() {
		return report_status;
	}
	public void setReport_status(String report_status) {
		this.report_status = report_status;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getRecv_time() {
		return recv_time;
	}
	public void setRecv_time(String recv_time) {
		this.recv_time = recv_time;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	@Override
	public String toString() {
		return "ReportVo [report_status=" + report_status + ", msgid=" + msgid + ", mobile=" + mobile + ", report="
				+ report + ", recv_time=" + recv_time + ", uid=" + uid + "]";
	}
}
