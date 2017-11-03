package com.dispatchtask.vo;
/**
 * 运营商号段表
 * @author Administrator
 *
 */
public class ProviderMobilePrefix {
	private int mob_pref_id;
	private int provider_id;
	private String mobile_prefix;
	public int getMob_pref_id() {
		return mob_pref_id;
	}
	public void setMob_pref_id(int mob_pref_id) {
		this.mob_pref_id = mob_pref_id;
	}
	public int getProvider_id() {
		return provider_id;
	}
	public void setProvider_id(int provider_id) {
		this.provider_id = provider_id;
	}
	public String getMobile_prefix() {
		return mobile_prefix;
	}
	public void setMobile_prefix(String mobile_prefix) {
		this.mobile_prefix = mobile_prefix;
	}
	
	
}
