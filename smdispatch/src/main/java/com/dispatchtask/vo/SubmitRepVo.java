package com.dispatchtask.vo;

import java.util.List;

public class SubmitRepVo {
	private String uid;
	private String status;
	private String respCode;
	private String respMsg;
	private int totalChargeNum;
	private List<ResultVo> result;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public int getTotalChargeNum() {
		return totalChargeNum;
	}
	public void setTotalChargeNum(int totalChargeNum) {
		this.totalChargeNum = totalChargeNum;
	}
	public List<ResultVo> getResult() {
		return result;
	}
	public void setResult(List<ResultVo> result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "SubmitRepVo [uid=" + uid + ", status=" + status + ", respCode=" + respCode + ", respMsg=" + respMsg
				+ ", totalChargeNum=" + totalChargeNum + ", result=" + result + "]";
	}
	
	
	
}
