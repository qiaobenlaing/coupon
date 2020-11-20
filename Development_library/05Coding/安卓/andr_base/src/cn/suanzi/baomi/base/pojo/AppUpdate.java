package cn.suanzi.baomi.base.pojo;

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
	 * 是否必须升级
	 */
	private String isMustUpdate;
	
	/**
	 * 新版本的下载地址
	 */
	private String updateUrl;
	
	/**
	 * 新的版本号
	 */
	private int newVersionCode;
	
	/**
	 * 可以更新
	 */
	private int canUpdate;
	
	/**
	 * 当前版本
	 */
	private int currentVersionCode;
	
	public int getCurrentVersionCode() {
		return currentVersionCode;
	}

	public void setCurrentVersionCode(int currentVersionCode) {
		this.currentVersionCode = currentVersionCode;
	}

	public int getNewVersionCode() {
		return newVersionCode;
	}

	public void setNewVersionCode(int newVersionCode) {
		this.newVersionCode = newVersionCode;
	}

	public int getCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(int canUpdate) {
		this.canUpdate = canUpdate;
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
