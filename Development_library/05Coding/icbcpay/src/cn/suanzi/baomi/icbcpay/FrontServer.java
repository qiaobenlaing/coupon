package cn.suanzi.baomi.icbcpay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.com.singlee.slice.format.StringFormat;
import cn.suanzi.baomi.icbcpay.persist.DB;
import cn.suanzi.baomi.icbcpay.pojo.IcbcLog;
import cn.suanzi.fastxmltools.XmlTool;
import cn.suanzi.fastxmltools.XmlToolException;
import cn.suanzi.tools.ExpressTool;
import cn.suanzi.tools.StringTool;

/**
 * Servlet implementation class FrontServer
 */
@WebServlet(description = "Icbc Quickpay front server", urlPatterns = { "/FrontServer" })
public class FrontServer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrontServer.class);
	
	private static final String  ICBC_SERVER = "12.1.1.117";
	private static final int ICBC_PORT = 12079;
	private static final int ICBC_TIMEOUT = 12000; // 设置超时为120s
	
	private static final String privateKeyPath = "./certificate/huiquanprivate.key";
	private static final String publicKeyPath = "./certificate/huiquanpublic.key";
	
	private RSASign rsaSign = null;
	private Connection cn = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontServer() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.rsaSign = new RSASign(request.getRealPath("/") + "WEB-INF/classes/" + privateKeyPath, 
        		request.getRealPath("/") + "WEB-INF/classes/" + publicKeyPath);
        
        request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		
		PrintWriter out = response.getWriter();
        
        log.debug("this.getServletContext.getRealPath(/):" + this.getServletContext().getRealPath("/"));
        
		String pub = request.getParameter("pub");
		String req = request.getParameter("req");
		// "<req><cardno>6212261986</cardno><username>&#x5F20;&#x5F6A;</username><idtype>0</idtype><idno>341622199007205377</idno><mobilephone>13738157214</mobilephone><userno>137381572141986</userno></req>";
		String fullReq = String.format("<package>%s%s</package>", pub, req);
		
		log.info("------------- RECEIVED A REQUEST ------------\n" + fullReq);

		//log.debug("request.getRealPath(/): " + request.getRealPath("/"));
		
		try {
			IcbcLog icbcLog = parsePub(pub);
			this.saveReq(icbcLog, fullReq);
			
			String ret = this.reqIcbc(pub, req);
			log.info("ICBC RETURNED: \n" + ret);
			
			// TODO verify signature
			
			this.saveRet(icbcLog, ret);
			out.write(ret);
			
			log.info("------------- #REQUEST FINISHED ------------");
			
		} catch (Exception e) {
			log.error("ERROR OCCURED.", e);
			out.write("<package><pub><retcode>-1</retcode><retmsg>Slice Internal Error</retmsg></<pub>></package>");
		}
	}
	
    /**
     * Test only, TODO remove it!!!
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	/**
	 * 解析请求的pub部分
	 * @param pub
	 * @return
	 * @throws XmlToolException 
	 */
	private IcbcLog parsePub(String pub) throws XmlToolException {
		//Map<String, String> pubMap = new HashMap<>();
		
		XmlTool xml = new XmlTool();
		xml.xmlDocumentCreate(pub);
		
		return new IcbcLog(
				xml.xmlQuery("/pub/cmptxsno"),
				xml.xmlQuery("/pub/txcode"),
				xml.xmlQuery("/pub/cityno"), 
				Integer.parseInt(xml.xmlQuery("/pub/cmpdate")),
				Integer.parseInt(xml.xmlQuery("/pub/cmptime")),
				Integer.parseInt(xml.xmlQuery("/pub/paytype")));
	}
	
	/**
	 * 向工行发起Socket支付请求
	 * @param pub
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	private String reqIcbc(String pub, String req) throws Exception {
		String sign = genIcbcSign(pub, req);
		
		String reqData = String.format("<?xml version=\"1.0\" encoding=\"GBK\"?><package>%s%s<signature>%s</signature></package>", 
				pub, req, sign);
		byte[] reqLenBytes = StringFormat.fmtFill(String.valueOf(reqData.length()).getBytes(), "0,10,LEFT");
		log.debug("REQ DATA LENGTH BYTES: \n" + StringTool.toHexTable(reqLenBytes));
				
		byte[] reqBytes = ExpressTool.getBytes(reqData);
		log.debug("REQUEST TO ICBC[" + reqBytes.length + "] : \n" + StringTool.toHexTable(reqBytes));
//		return "";
		
		try (Socket sock = new Socket(ICBC_SERVER, ICBC_PORT);
				OutputStream out = sock.getOutputStream();
				InputStream in = sock.getInputStream()) {

			sock.setSoTimeout(ICBC_TIMEOUT); // 最多等待60s
			
			// 发送请求
			out.write(reqLenBytes);
			out.write(reqBytes);
			out.flush();
//			OutputStreamWriter opsw = new OutputStreamWriter(out);
//	        BufferedWriter bw = new BufferedWriter(opsw);
//	        bw.write(reqData);
//	        bw.flush();

//	        log.debug("IS SOCKET CLOSED @ 2: " + sock.isClosed() + ", " + sock.isInputShutdown() + ", " + sock.isOutputShutdown());
			
			byte[] retLenBytes = new byte[10];
			in.read(retLenBytes);
			log.debug("RAW RESPONSE LENGTH FROM ICBC [" + 10 + "] : \n" + StringTool.toHexTable(retLenBytes));
			
//	        log.debug("IS SOCKET CLOSED @ 3: " + sock.isClosed() + ", " + sock.isInputShutdown() + ", " + sock.isOutputShutdown());
			
			int retLen = Integer.parseInt(new String(retLenBytes));
			// 接收返回数据
			byte[] retBytes = new byte[retLen];
			in.read(retBytes);
			log.debug("RAW RESPONSE FROM ICBC[" + retLen + "] : \n"
					+ StringTool.toHexTable(retBytes));
			String ret = new String(retBytes, "GBK").replace("GBK", "UTF-8");
			return ret;
		}
		
		
//		Socket sock = null;
//		try {
//			InetAddress addr = InetAddress.getByName(ICBC_SERVER);
//			SocketAddress sockaddr = new InetSocketAddress(addr, ICBC_PORT);
//			if (sock == null) {
//				sock = new Socket();
//			}
//			sock.connect(sockaddr, ICBC_TIMEOUT);
//			OutputStream out = sock.getOutputStream();
//			
//			// 发送请求
//			out.write(ExpressTool.getBytes(reqData));
//			out.flush();
//			
//			// 接收返回数据长度
//			InputStream in = sock.getInputStream();
//			byte[] retBytes = SocketTool.readStream(in, convertStringToCharArray("</package>"));
//			int len = retBytes.length;
//			log.debug("RAW RESPONSE FROM ICBC[" + len + "] : \n"
//								+ StringTool.toHexTable(retBytes));
//			
//			String ret = new String(retBytes, "GBK");
//			ret = ret.substring(10); // 截掉前面10个字符（报文长度）。
//			return ret;
//		} finally {
//			if (sock != null) {
//				try {
//					sock.close();
//				} catch (Exception e) {
//					log.warn("ERROR AT CLOSING SOCKET.", e);
//				}
//			}
//		}
		
		// TEST ONLY
//		return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + '\n'
//					+ "<package><pub><txcode>20100</txcode><cityno>1202</cityno><retcode>00000</retcode><retmsg>���׳ɹ�</retmsg><settledate>20151118</settledate></pub><ans><cmptxsno>15111814512000010</cmptxsno><trxserno>0</trxserno><result></result><amount></amount><varinfo></varinfo></ans>"
//					+ "<signature>vQ8tEYQQOMlGChOcFDpunsvVDdWLkk1AjnJkQMoLIf8+/V3b3znIZGgqDC5OT1qoS4hzybxQl+xz"
//					+ "qg8pBvT8zaW06TQoDDhbt5Rb+/EXUlhC83ObKy4bVihiyHAgwnT+rlLoogbvFzr4hyMzkn9EOPqJ5wkphPPegXW9IRi2wSY=</signature></package>";
	}
	
	/** 
	 * 工行签名
	 * @param pub
	 * @param req
	 * @return
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	private String genIcbcSign(String pub, String req) throws UnsupportedEncodingException, Exception {
		return rsaSign.signByPrivateKey((pub + req).getBytes("UTF-8"));
	}
	
	/**
	 * 记录请求流水到DB
	 * @param pubMap
	 * @param pubStr
	 * @param req
	 * @return
	 * @throws SQLException 
	 */
	private int saveReq(IcbcLog icbcLog, String fullReq) throws SQLException {
		Connection cn = getConn();
		try (
			PreparedStatement stat = cn.prepareStatement("REPLACE INTO log (cmptxsno, txcode, cityno, cmpdate, cmptime, paytype, reqbody) VALUES (?,?,?,?,?,?,?)")) {
			int i = 0;
			stat.setString(++i, icbcLog.getCmptxsno());
			stat.setString(++i, icbcLog.getTxcode());
			stat.setString(++i, icbcLog.getCityno());
			stat.setInt(++i, icbcLog.getCmpdate());
			stat.setInt(++i, icbcLog.getCmptime());
			stat.setInt(++i, icbcLog.getPaytype());
			stat.setString(++i, fullReq);
			
			return stat.executeUpdate();
		}
	}

	
	/**
	 * 记录工行返回结果流水到DB
	 * @param pubMap
	 * @param ret
	 * @return
	 * @throws XmlToolException 
	 * @throws SQLException 
	 */
	private int saveRet(IcbcLog icbcLog, String ret) throws XmlToolException, SQLException {

		XmlTool xml = new XmlTool();
		xml.xmlDocumentCreate(ret);
		icbcLog.setRetcode(xml.xmlQuery("/package/pub/retcode"));
		icbcLog.setRetmsg(xml.xmlQuery("/package/pub/retmsg"));
		try {
			icbcLog.setSettledate(Integer.parseInt(xml.xmlQuery("/package/pub/settledate")));
		} catch (Exception e) {
			icbcLog.setSettledate(0);
		}
		
		Connection cn = getConn();
		try (
			PreparedStatement stat = cn.prepareStatement("UPDATE log SET settledate=?, retcode=?, retmsg=?, retbody=? WHERE cmptxsno=?")) {
			int i = 0;
			stat.setInt(++i, icbcLog.getSettledate());
			stat.setString(++i, icbcLog.getRetcode());
			stat.setString(++i, icbcLog.getRetmsg());
			stat.setString(++i, ret);
			stat.setString(++i, icbcLog.getCmptxsno());
			
			return stat.executeUpdate();
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		DB.closeCn(this.cn);
	}
	
	/**
	 * 单例模式获取数据库链接
	 * @return
	 * @throws SQLException
	 */
	private Connection getConn() throws SQLException {
		if (cn == null) {
			cn = DB.getConn();
		}
		return cn;
	}
	

	public static byte[] convertStringToCharArray(String str)
			throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				if (str.length() == i + 1) {
					bout.write(92);
				} else if ((str.length() >= i + 3)
						&& (str.charAt(i + 1) == '0')
						&& ((str.charAt(i + 2) == 'x') || (str.charAt(i + 2) == 'X'))) {
					bout.write(str.substring(i + 1, i + 3).getBytes());

					i++;
				} else if (str.charAt(i + 1) == 'n') {
					bout.write(10);
				} else if (str.charAt(i + 1) == 't') {
					bout.write(9);
				} else if (str.charAt(i + 1) == 'r') {
					bout.write(13);
				} else if (str.charAt(i + 1) == 'f') {
					bout.write(12);
				} else if (str.charAt(i + 1) == '\\') {
					bout.write(92);
				} else if (str.charAt(i + 1) == '0') {
					bout.write(0);
				} else {
					throw new IllegalArgumentException("该字符串[" + str
							+ "]格式不正确!");
				}
				i++;
			} else if ((str.charAt(i) == '0')
					&& (str.length() >= i + 4)
					&& ((str.charAt(i + 1) == 'x') || (str.charAt(i + 1) == 'X'))) {
				bout.write(Integer.parseInt(str.substring(i + 2, i + 4), 16));
				i += 3;
			} else {
				bout.write(str.charAt(i));
			}
		}
		return bout.toByteArray();
	}

}
