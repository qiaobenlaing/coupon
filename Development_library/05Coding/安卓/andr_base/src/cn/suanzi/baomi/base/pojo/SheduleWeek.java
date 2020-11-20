package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * 教育行业的课程表的上课时间
 * @author yingchen
 *
 */
public class SheduleWeek implements Serializable{
	/**周几*/
	private String  weekName;
	/**每天的上课时间*/
	private List<SheduleDay>  learnTime;
	public SheduleWeek() {
		super();
	}
	public SheduleWeek(String weekName, List<SheduleDay> learnTime) {
		super();
		this.weekName = weekName;
		this.learnTime = learnTime;
	}
	public String getWeekName() {
		return weekName;
	}
	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}
	public List<SheduleDay> getLearnTime() {
		return learnTime;
	}
	public void setLearnTime(List<SheduleDay> learnTime) {
		this.learnTime = learnTime;
	}
	
	
}
