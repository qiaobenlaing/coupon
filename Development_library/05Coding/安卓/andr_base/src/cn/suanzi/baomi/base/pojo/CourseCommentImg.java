package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 课程评价的图片
 * @author yingchen
 *
 */
public class CourseCommentImg implements Serializable{
	private String  remarkImgUrl;

	public CourseCommentImg() {
		super();
	}

	public CourseCommentImg(String remarkImgUrl) {
		super();
		this.remarkImgUrl = remarkImgUrl;
	}

	public String getRemarkImgUrl() {
		return remarkImgUrl;
	}

	public void setRemarkImgUrl(String remarkImgUrl) {
		this.remarkImgUrl = remarkImgUrl;
	}
	
	
	
}
