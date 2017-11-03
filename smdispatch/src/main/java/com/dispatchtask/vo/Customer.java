package com.dispatchtask.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Customer implements Serializable{
	private static final long serialVersionUID = -6313028029923165040L;
	private int cust_id;// number(8) n 客户ID
	private String cust_code;//客户代码
	private String cust_name;// varchar2(50) N 客户名称
	private String passwd;// varchar2(20) N 密码
	private String client_ip;// varchar2(50) Y IP鉴权
	private String audit_flag;// char(1) n 是否需要审核
	private int parent_id;// number(8) y 上级客户
	private String status;// char(1) n 状态
	private String create_time;// date n 创建时间
	private String create_by;// varchar2(20) Y 创建人
	private BigDecimal amount;
	private long sms_balance;
	private List<String> blacklist_filter;
	private List<String> sensitive_words_filter;  //过滤敏感词 1 网关敏感词 2 客户敏感词
	private String sensitive_words_policy;  //命中敏感词 后续处理策略 1 拦截  2 进入内容人工审核
	private int threads;
	private List<String> ipList = new ArrayList<String>();
	private int send_rate = 100;// number(3) y 发送速度
	
	
	
	public int getSend_rate() {
		return send_rate;
	}

	public void setSend_rate(int send_rate) {
		this.send_rate = send_rate;
	}

	public int getCust_id() {
		return cust_id;
	}

	public void setCust_id(int cust_id) {
		this.cust_id = cust_id;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
		if (client_ip != null && !"*".equals(client_ip)) {
			String[] ips = client_ip.split(",");
			List<String> list = Arrays.asList(ips);
			ipList.addAll(list);
			ipList.add("127.0.0.1");
		}
	}
	
	public List<String> getIpList(){
		return this.ipList;
	}

	public String getAudit_flag() {
		return audit_flag;
	}

	public void setAudit_flag(String audit_flag) {
		this.audit_flag = audit_flag;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public void setCust_code(String cust_code) {
		this.cust_code = cust_code;
	}

	public String getCust_code() {
		return cust_code;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setSms_balance(long sms_balance) {
		this.sms_balance = sms_balance;
	}

	public long getSms_balance() {
		return sms_balance;
	}

	public List<String> getBlacklist_filter() {
		return blacklist_filter;
	}

	public void setBlacklist_filter(String blacklistfilter) {
		if(blacklistfilter == null 
				|| blacklistfilter.trim().length()==0
				|| blacklistfilter.equals("0")){
			this.blacklist_filter = new ArrayList<String>();
		}else{
			String[] array = blacklistfilter.split(",");
	     	this.blacklist_filter = Arrays.asList(array);
		}
	}

	public List<String> getSensitive_words_filter() {
		return sensitive_words_filter;
	}

	public void setSensitive_words_filter(String sensitive_words_filter) {
		if(sensitive_words_filter == null 
				|| sensitive_words_filter.trim().length()==0
				|| sensitive_words_filter.equals("0")){
			this.sensitive_words_filter = new ArrayList<String>();
		}else{
			String[] array = sensitive_words_filter.split(",");
	     	this.sensitive_words_filter = Arrays.asList(array);
		}
		
		if(this.sensitive_words_filter == null){
			this.sensitive_words_filter = new ArrayList<String>();
		}
	}

	public String getSensitive_words_policy() {
		return sensitive_words_policy;
	}

	public void setSensitive_words_policy(String sensitive_words_policy) {
		this.sensitive_words_policy = sensitive_words_policy;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}
}
