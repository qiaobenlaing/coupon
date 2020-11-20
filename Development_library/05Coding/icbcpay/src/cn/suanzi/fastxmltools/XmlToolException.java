package cn.suanzi.fastxmltools;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XmlToolException extends Exception {
	/**
	 * @var long
	 */
	private static final long serialVersionUID = -7880000095598676998L;
	private Throwable _exception;

	public XmlToolException() {
	}

	public XmlToolException(String message) {
		super(message);
	}

	public XmlToolException(String message, Throwable cause) {
		this(message);
		this._exception = cause;
	}

	public XmlToolException(Throwable cause) {
		this._exception = cause;
	}

	public Throwable getException() {
		return this._exception;
	}

	public void printStackTrace(PrintStream s) {
		synchronized (s) {
			if (this._exception != null) {
				s.print(getClass().toString());
				this._exception.printStackTrace(s);
			} else {
				super.printStackTrace(s);
			}
		}
	}

	public void printStackTrace(PrintWriter s) {
		synchronized (s) {
			if (this._exception != null) {
				s.print(getClass().toString());
				this._exception.printStackTrace(s);
			} else {
				super.printStackTrace(s);
			}
		}
	}
}
