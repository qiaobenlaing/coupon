package cn.suanzi.fastxmltools;

public class WriteSourceException extends XmlToolException {
	/**
	 * @var long
	 */
	private static final long serialVersionUID = 1209366584911980519L;

	public WriteSourceException() {
	}

	public WriteSourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public WriteSourceException(Throwable cause) {
		super(cause);
	}

	public WriteSourceException(String message) {
		super(message);
	}
}
