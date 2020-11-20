package cn.com.singlee.slice.format;


public class DataFieldValidate {
	public static byte[] validateLength(byte[] bInt, String params)
			throws Exception {
		int point = params.indexOf(",");
		if (point < 0) {
			throw new Exception("报文配置有错，请检查");
		}
		String min_length = params.substring(0, point);
		String max_length = params.substring(point + 1, params.length());
		int min = 0;
		int max = 0;
		try {
			if (!min_length.equals("")) {
				min = Integer.parseInt(min_length.trim());
			}
			max = Integer.parseInt(max_length.trim());
		} catch (NumberFormatException e) {
			throw new Exception("报文配置有错，请检查");
		}
		if ((max < min) || (min < 0) || (max < 0)) {
			throw new Exception("报文配置有错，请检查");
		}
		if (bInt.length > max) {
			throw new Exception("字段长度不合要求，最大长度为：" + max);
		}
		if (bInt.length < min) {
			throw new Exception("字段长度不合要求，最小长度为：" + min);
		}
		return bInt;
	}

	public static byte[] valiadateType(byte[] bInt, String params)
			throws Exception {
		if (params.trim().equals("整型")) {
			try {
				Integer.parseInt(new String(bInt));
			} catch (NumberFormatException e) {
				throw new Exception("字段类型不合要求，要求该字段为：" + params);
			}
		} else if (params.trim().equals("数值型")) {
			try {
				Float.parseFloat(new String(bInt));
			} catch (NumberFormatException e) {
				throw new Exception("字段类型不合要求，要求该字段为：" + params);
			}
		}
		return bInt;
	}

	public static void main(String[] args) throws Exception {
		String a = ",5";
		String b = "压力测试";
		validateLength(b.getBytes(), a);
	}
}
