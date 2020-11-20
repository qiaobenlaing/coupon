package cn.suanzi.fastxmltools;

public class LXpathParser {
	private String _xPath;
	private int _cur;
	private int _len;
	private String _token;
	public static final int FUNC = 4;
	public static final int NODE = 3;
	public static final int ATTR = 2;
	public static final int END = -1;

	public void parse(String sXPath) {
		this._xPath = sXPath;
		this._cur = 0;
		this._len = this._xPath.length();
		this._token = this._xPath;
	}

	public String getToken() {
		return this._token;
	}

	public int token() {
		if (this._cur == this._len) {
			return LXpathParser.END;
		}
		char ch = this._xPath.charAt(this._cur);
		if (Character.isLetter(ch)) {
			int nStart = this._cur;
			while (this._cur < this._len) {
				ch = this._xPath.charAt(this._cur);
				if (ch == '(') {
					this._token = this._xPath.substring(nStart, this._cur);
					return LXpathParser.FUNC;
				}
				this._cur += 1;
			}
		} else if (ch == '/') {
			int nStart = this._cur;
			while (this._cur < this._len) {
				ch = this._xPath.charAt(this._cur++);
				if ((this._cur == this._len) || (ch == ')')) {
					if (ch == ')') {
						this._token = this._xPath.substring(nStart,
								this._cur - 1);
					} else {
						this._token = this._xPath.substring(nStart, this._cur);
					}
					int nLastDeli = this._token.lastIndexOf("/");
					if (this._token.charAt(nLastDeli + 1) == '@') {
						return LXpathParser.ATTR;
					}
					return LXpathParser.NODE;
				}
			}
		} else {
			if (ch == '(') {
				this._cur += 1;
				return token();
			}
			if (Character.isWhitespace(ch)) {
				this._cur += 1;
				return token();
			}
		}
		return LXpathParser.END;
	}
}
