package cn.suanzi.baomi.icbcpay.pojo;

public class IcbcLog {
	
	private String cmptxsno;
	private String txcode;
	private String cityno;
	private int cmpdate;
	private int cmptime;
	private int paytype;
	private String reqbody;
	private int settledate;
	private String retcode;
	private String retmsg;
	private String retbody;
	
	/**
	 * 
	 */
	public IcbcLog() {
		super();
	}
	
	/**
	 * @param cmptxsno
	 * @param txcode
	 * @param cityno
	 * @param cmpdate
	 * @param cmptime
	 * @param paytype
	 */
	public IcbcLog(String cmptxsno, String txcode, String cityno, int cmpdate, int cmptime, int paytype) {
		super();
		this.cmptxsno = cmptxsno;
		this.txcode = txcode;
		this.cityno = cityno;
		this.cmpdate = cmpdate;
		this.cmptime = cmptime;
		this.paytype = paytype;
	}

	/**
	 * @return the cmptxsno
	 */
	public String getCmptxsno() {
		return cmptxsno;
	}
	/**
	 * @param cmptxsno the cmptxsno to set
	 */
	public void setCmptxsno(String cmptxsno) {
		this.cmptxsno = cmptxsno;
	}
	/**
	 * @return the txcode
	 */
	public String getTxcode() {
		return txcode;
	}
	/**
	 * @param txcode the txcode to set
	 */
	public void setTxcode(String txcode) {
		this.txcode = txcode;
	}
	/**
	 * @return the cityno
	 */
	public String getCityno() {
		return cityno;
	}
	/**
	 * @param cityno the cityno to set
	 */
	public void setCityno(String cityno) {
		this.cityno = cityno;
	}
	/**
	 * @return the cmpdate
	 */
	public int getCmpdate() {
		return cmpdate;
	}
	/**
	 * @param cmpdate the cmpdate to set
	 */
	public void setCmpdate(int cmpdate) {
		this.cmpdate = cmpdate;
	}
	/**
	 * @return the cmptime
	 */
	public int getCmptime() {
		return cmptime;
	}
	/**
	 * @param cmptime the cmptime to set
	 */
	public void setCmptime(int cmptime) {
		this.cmptime = cmptime;
	}
	/**
	 * @return the paytype
	 */
	public int getPaytype() {
		return paytype;
	}
	/**
	 * @param paytype the paytype to set
	 */
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}
	/**
	 * @return the reqbody
	 */
	public String getReqbody() {
		return reqbody;
	}
	/**
	 * @param reqbody the reqbody to set
	 */
	public void setReqbody(String reqbody) {
		this.reqbody = reqbody;
	}
	/**
	 * @return the settledate
	 */
	public int getSettledate() {
		return settledate;
	}
	/**
	 * @param settledate the settledate to set
	 */
	public void setSettledate(int settledate) {
		this.settledate = settledate;
	}
	/**
	 * @return the retcode
	 */
	public String getRetcode() {
		return retcode;
	}
	/**
	 * @param retcode the retcode to set
	 */
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	/**
	 * @return the retmsg
	 */
	public String getRetmsg() {
		return retmsg;
	}
	/**
	 * @param retmsg the retmsg to set
	 */
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	/**
	 * @return the retbody
	 */
	public String getRetbody() {
		return retbody;
	}
	/**
	 * @param retbody the retbody to set
	 */
	public void setRetbody(String retbody) {
		this.retbody = retbody;
	}
	
	
}
