package com.dispatchtask.vo;

public class EfdSmrpt {
	private long rptid;
	private long downid;
	private long sm_serialno;
	private String sm_recvphone;
	private String sm_content;
	private int sm_ismgtype;
	private String sm_servicecode;
	private String sm_sendtime;
	private String sm_batchno;
	private String sm_servicename;
	private String downtime;
	private String sendcode;
	private int sendlevel;
	private long gatewayno;
	private String messageid;
	private String sendtime;
	private int res_status;
	private String res_time;
	private int rpt_status;
	private String rpt_time;
	private String rpt_code;
	private String subtime;
	private int pknumber;
	private int pktotal;
	private String markinfo;
	private int upTime;
	
	
	
	public int getUpTime() {
		return upTime;
	}

	public void setUpTime(int upTime) {
		this.upTime = upTime;
	}

	public EfdSmrpt() {
		
	}
	
	public EfdSmrpt(Smdown smdown) {
		this.downid = smdown.getDownid();
		int index =  smdown.getSm_serialphones().indexOf(":");
		this.sm_serialno = Long.valueOf(smdown.getSm_serialphones().substring(0, index));
		this.sm_ismgtype = smdown.getSm_ismgtype();
		this.sm_servicecode = smdown.getSm_servicecode();
		this.sendlevel = smdown.getSendlevel();
		this.sm_content = smdown.getSm_content();
		this.downtime = smdown.getDowntime();
		this.sm_batchno = smdown.getSm_batchno();
		this.sm_servicename = smdown.getSm_servicename();
		
		if(smdown.getSm_servicecode()==null || "".equals(smdown.getSm_servicecode())){
			this.sendcode = "0";
		}else{
			this.sendcode = smdown.getSm_servicecode();
		}
	}
	public long getRptid() {
		return rptid;
	}
	public void setRptid(long rptid) {
		this.rptid = rptid;
	}
	public long getDownid() {
		return downid;
	}
	public void setDownid(long downid) {
		this.downid = downid;
	}
	public long getSm_serialno() {
		return sm_serialno;
	}
	public void setSm_serialno(long sm_serialno) {
		this.sm_serialno = sm_serialno;
	}
	public void setSm_serialno(int sm_serialno) {
		this.sm_serialno = sm_serialno;
	}
	public String getSm_recvphone() {
		return sm_recvphone;
	}
	public void setSm_recvphone(String sm_recvphone) {
		this.sm_recvphone = sm_recvphone;
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
	public String getSendcode() {
		return sendcode;
	}
	public void setSendcode(String sendcode) {
		this.sendcode = sendcode;
	}
	public int getSendlevel() {
		return sendlevel;
	}
	public void setSendlevel(int sendlevel) {
		this.sendlevel = sendlevel;
	}
	public long getGatewayno() {
		return gatewayno;
	}
	public void setGatewayno(long gatewayno) {
		this.gatewayno = gatewayno;
	}
	public String getMessageid() {
		return messageid;
	}
	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public int getRes_status() {
		return res_status;
	}
	public void setRes_status(int res_status) {
		this.res_status = res_status;
	}
	public String getRes_time() {
		return res_time;
	}
	public void setRes_time(String res_time) {
		this.res_time = res_time;
	}
	public int getRpt_status() {
		return rpt_status;
	}
	public void setRpt_status(int rpt_status) {
		this.rpt_status = rpt_status;
	}
	public String getRpt_time() {
		return rpt_time;
	}
	public void setRpt_time(String rpt_time) {
		this.rpt_time = rpt_time;
	}
	public String getRpt_code() {
		return rpt_code;
	}
	public void setRpt_code(String rpt_code) {
		this.rpt_code = rpt_code;
	}
	public String getSubtime() {
		return subtime;
	}
	public void setSubtime(String subtime) {
		this.subtime = subtime;
	}
	public int getPknumber() {
		return pknumber;
	}
	public void setPknumber(int pknumber) {
		this.pknumber = pknumber;
	}
	public int getPktotal() {
		return pktotal;
	}
	public void setPktotal(int pktotal) {
		this.pktotal = pktotal;
	}
	public String getMarkinfo() {
		return markinfo;
	}
	public void setMarkinfo(String markinfo) {
		this.markinfo = markinfo;
	}
	
	
	


}
