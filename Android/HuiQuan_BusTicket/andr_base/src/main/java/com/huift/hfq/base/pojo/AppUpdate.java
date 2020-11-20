package com.huift.hfq.base.pojo;

import java.io.Serializable;

/**
 * app升级
 * @author ad
 */
public class AppUpdate implements Serializable {
	
	/**
	 * 版本号
	 */
	private String versionCode;
	
	/**
	 * 是否必须升级,默认非必须
	 */
	private String isMustUpdate="0";
	
	/**
	 * 新版本的下载地址
	 */
	private String updateUrl;
	
	/**
	 * 更新说明
	 */
	private String updateContent;

	public String getUpdateContent() {
		return updateContent;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getIsMustUpdate() {
		return isMustUpdate;
	}

	public void setIsMustUpdate(String isMustUpdate) {
		this.isMustUpdate = isMustUpdate;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	
	
}
