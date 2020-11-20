package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/***
 * 教育行业的每/周/月之星
 * @author yingchen
 *
 */
public class EveryStar implements Serializable{
	private String  starCode;
	private String  starInfo;
	private String  starUrl;
	private String 	starName;//是每日 还是每周
	private List<EveryStartWork> starWork;
	private int  studentAge;
	private String studentGrade;
	private String studentName;
	
	public EveryStar() {
		super();
	}

	public EveryStar(String starCode, String starInfo, String starUrl, String starName, List<EveryStartWork> starWork, int studentAge, String studentGrade, String studentName) {
		super();
		this.starCode = starCode;
		this.starInfo = starInfo;
		this.starUrl = starUrl;
		this.starName = starName;
		this.starWork = starWork;
		this.studentAge = studentAge;
		this.studentGrade = studentGrade;
		this.studentName = studentName;
	}



	public String getStarCode() {
		return starCode;
	}


	public void setStarCode(String starCode) {
		this.starCode = starCode;
	}


	public String getStarInfo() {
		return starInfo;
	}


	public void setStarInfo(String starInfo) {
		this.starInfo = starInfo;
	}


	public String getStarUrl() {
		return starUrl;
	}


	public void setStarUrl(String starUrl) {
		this.starUrl = starUrl;
	}

	public String getStarName() {
		return starName;
	}

	public void setStarName(String starName) {
		this.starName = starName;
	}

	public List<EveryStartWork> getStarWork() {
		return starWork;
	}

	public void setStarWork(List<EveryStartWork> starWork) {
		this.starWork = starWork;
	}

	public int getStudentAge() {
		return studentAge;
	}

	public void setStudentAge(int studentAge) {
		this.studentAge = studentAge;
	}

	public String getStudentGrade() {
		return studentGrade;
	}

	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	
	
}
