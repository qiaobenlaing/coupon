package com.whhft.sysmanage.common.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.whhft.sysmanage.common.entity.ServerInfo;

public class ServerInfoExt extends ServerInfo{
	private List<DiskInfo> diskInfoList = new ArrayList<DiskInfo>();

	public List<DiskInfo> getDiskInfoList() {
		return diskInfoList;
	}

	public void setDiskInfoList(List<DiskInfo> diskInfoList) {
		this.diskInfoList = diskInfoList;
	}
	
	public void addDiskInfo(DiskInfo diskInfo) {
		this.diskInfoList.add(diskInfo);
	}
	
	public ServerInfo toServerInfo(){
		ServerInfo si = new ServerInfo();
		BeanUtils.copyProperties(this, si);
		return si;
	}
}
