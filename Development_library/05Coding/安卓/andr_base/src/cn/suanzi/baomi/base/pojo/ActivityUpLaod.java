package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 编辑活动图文页对应的实体类
 * @author ad
 *
 */
public class ActivityUpLaod implements Serializable{
	
	private int type; //0---文字  1---图片
	
	private int position; //位置
	
	private String content; //文本内容
	
	private String loaclPath; //图片本地路径
	
	private String netPath ;//图片的上传服务器以后的地址

	public ActivityUpLaod() {
		super();
	}

	public ActivityUpLaod(int type, int position, String content, String loaclPath, String netPath) {
		super();
		this.type = type;
		this.position = position;
		this.content = content;
		this.loaclPath = loaclPath;
		this.netPath = netPath;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLoaclPath() {
		return loaclPath;
	}

	public void setLoaclPath(String loaclPath) {
		this.loaclPath = loaclPath;
	}

	public String getNetPath() {
		return netPath;
	}

	public void setNetPath(String netPath) {
		this.netPath = netPath;
	}

	@Override
	public String toString() {
		return "ActivityUpLaod [type=" + type + ", position=" + position + ", content=" + content + ", loaclPath=" + loaclPath + ", netPath=" + netPath + "]";
	}
	
	
	
}
