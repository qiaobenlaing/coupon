package cn.suanzi.baomi.base;

/**
 * APP系统通用异常代码，如果是单个API独有的异常，无需定义在此，可以定义在调用API类的Activity中。
 *
 * @author Weiping
 * @version 1.0.0	2014-11-02	创建
 */
public class ErrorCode {
	/** 一切正常 */
	public static final int SUCC = 50000;
	public static final int FAIL = 00000;
	
	// ---------- 系统异常 ---------------
	/** 服务端（API）内部异常 */
	public static final int API_INTERNAL_ERR = 20000;
	/** 客户端内部异常。 */
	public static final int APP_INTERNAL_ERR = 20001;
	/** 客户端未联网 */
	public static final int CLI_NET_NOT_CONN = 20102;
	/** 网络异常（服务端或者客户端突然网络异常） */
	public static final int NETWORK_PROBLEM = 20103;
	/** 会话令牌超时失效，需重新登录 */
	public static final int USER_SESSION_OUT = 20204;
	/** 会话验证错误（令牌token非法，或者vcode非法），需跳转至登录页面 */
	public static final int USER_TOKEN_INVALID = 20205;
	/** 用户无权限访问资源，需要登录 */
	public static final int USER_NOT_AUTHORIZED = 20206;
	/** 用户自动登录失败 */
	public static final int USER_AUTO_LOGIN_FAIL = 20210;
	/** 失败的code 0*/
	public static final int ERROR_RET_CODE = 0;
	/** 成功的code 1*/
	public static final int RIGHT_RET_CODE = 1;
	
	
	// ---------- JSON-RPC ---------------
	/** Invalid Request, The JSON sent is not a valid Request object. */
	public static final int JSONRPC_INVALID_REQUEST = -32600;
	/** Method not found, The method does not exist / is not available. */
	public static final int JSONRPC_METHOD_NOT_FOUND = -32601;
	/** Invalid params, Invalid method parameter(s). */
	public static final int JSONRPC_INVALID_PARAMS = -32602;
	/** Internal error	Internal JSON-RPC error. */
	public static final int JSONRPC_INTERNAL_ERROR = -32603;
	/** Parse error, Invalid Json. */
	public static final int JSONRPC_INVALID_JSON = -32700;
	
	// ---------- 用户操作相关异常 ---------------
	/** 用户不存在 */
	public static final int USER_NOT_EXISTED = 20207;
	/** 密码不能为空*/
	public static final int USER_PASS_NOTHING = 60010;
	/** 用户被禁用（或者未启用） */
	public static final int USER_DISABLED = 20208;
	/** 用户密码不匹配*/
	public static final int USER_PASS_INVALID = 60011;
	/** 用户需要去激活账号 */
	public static final int USER_PASS_ACTIVATE = 80047;
	/** 图片格式不正确*/
	public static final int PIC_UPLOAD_FORM = 80020;
	/** 图片大小不正确*/
	public static final int PIC_UPLOAD_SIZE = 80021;
	
	/** 主异常Code，对应消息显示 */
	private int code = SUCC;
	/** 子异常Code，对应具体异常 */
	private int subCode = SUCC;
	
	/**
	 * 根据错误/异常代码获取对应的消息 
	 * @param code 错误或异常代码
	 * @return 对应的错误或异常消息 
	 */
	public static String getMsg(int code) {
		return "程序出现错误。代码：" + code;
	}
	
	/**
	 * @param code 主异常Code 
	 */
	public ErrorCode(int code) {
		this.code = code;
	}
	
	/**
	 * @param code 主异常Code 
	 * @param subCode 子异常Code
	 */
	public ErrorCode(int code, int subCode) {
		this.code = code;
		this.subCode = subCode;
	}
	
	
	/**
	 * @return 主异常Code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return 子异常Code
	 */
	public int getSubCode() {
		return subCode;
	}
	
	@Override
	public String toString() {
		return String.format("{Code: %s, SubCode: %s}", code, subCode);
	}
}
