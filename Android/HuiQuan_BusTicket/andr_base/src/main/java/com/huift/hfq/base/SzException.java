// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2014 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package com.huift.hfq.base;


/**
 * 服务请求异常类。在本系统中将重点使用错误代码，而弱化异常类中自带的消息，因为该消息不好国际化。
 * 
 * @author Weiping Liu
 * @version 1.0.0
 */
public class SzException extends Exception {

	private static final long serialVersionUID = 1L;
	/** 默认异常消息 */
	private static final String DEFAULT_ERR_MSG = "An error occured.";
	/** 异常代码。其中，主错误代码，主要用于获取对应对用户友好的错误消息给用户。
	 * 子错误代码，主要反馈给技术人员快速跟踪错误，不对应错误消息。
	 * 如果没有设置子错误代码时，为0. 如果存在子错误代码，在校错误消息的末尾附上子错误代码，否则附上主错误代码 */
	private ErrorCode errCode = null;

	/*public XNServiceException(String message) {
        super(message);
    }*/

    public SzException(int code) {
        super(DEFAULT_ERR_MSG);
        this.errCode = new ErrorCode(code);
    }

//    public SzException(int code, int subCode) {
//        super(DEFAULT_ERR_MSG);
//        this.errCode = new ErrorCode(code, subCode);
//    }
    
    public SzException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public SzException(String message) {
        super(message);
    }

    public SzException(int code, Throwable throwable) {
        super(DEFAULT_ERR_MSG, throwable);
        this.errCode = new ErrorCode(code);
    }

//    public SzException(int code, int subCode, Throwable throwable) {
//        super(DEFAULT_ERR_MSG, throwable);
//        this.errCode = new ErrorCode(code, subCode);
//    }

    public SzException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.errCode = new ErrorCode(code);
    }

//    public SzException(int code, int subCode, String message, Throwable throwable) {
//        super(message, throwable);
//        this.errCode = new ErrorCode(code, subCode);
//    }
    
    /**
	 * @return 异常代码
	 */
	public ErrorCode getErrCode() {
		return errCode;
	}

	/**
     * 获取该异常错误代码
     * @return 该异常的错误代码。
     */
    public int getCode() {
    	return this.errCode.getCode();
    }
    
//    /**
//     * 获取该异常的子错误代码
//     * @return
//     */
//    public int getSubCode() {
//    	return this.errCode.getSubCode();
//    }
}
