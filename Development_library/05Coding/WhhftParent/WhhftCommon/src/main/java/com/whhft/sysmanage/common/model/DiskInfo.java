/***********************************************************************
 * Module:  DiskInfo.java
 * Author:  user
 * Purpose: Defines the Class DiskInfo
 ***********************************************************************/

package com.whhft.sysmanage.common.model;

public class DiskInfo {
	private long diskInfoId;
	private java.lang.String diskPath;
	private long diskSize;
	private double diskUseRate;
	private java.lang.String type;

	public long getDiskInfoId() {
		return diskInfoId;
	}

	public void setDiskInfoId(long diskInfoId) {
		this.diskInfoId = diskInfoId;
	}

	public java.lang.String getDiskPath() {
		return diskPath;
	}

	public void setDiskPath(java.lang.String diskPath) {
		this.diskPath = diskPath;
	}

	public long getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(long diskSize) {
		this.diskSize = diskSize;
	}

	public double getDiskUseRate() {
		return diskUseRate;
	}

	public void setDiskUseRate(double diskUseRate) {
		this.diskUseRate = diskUseRate;
	}

	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

}