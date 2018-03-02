package pers.bertram.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class StringUtil {
	public static String codeTransfer(String str,  String toCoder) {
		String res = str;
		if (res == null)
			return "";
		try {
			byte[] buf = str.getBytes("iso-8859-1");
			res = new String(buf, toCoder);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static String codeTransfer(String str) {
		return codeTransfer(str, "utf-8");
	}
	public static String encodeURI(String str) {
		if (str == null)
			return "";
		String res = str;
		try {
			res = URLEncoder.encode(str, "utf-8");
		} catch (Exception e) { e.printStackTrace(); }
		return res;
	}
	
	public static void logFile(byte[] buf, int len, File f) throws Exception {
		OutputStream os = new FileOutputStream(f);
		String st = new BigInteger(1, buf).toString(16);
		StringBuilder s = new StringBuilder();
		int i = 0;
		for (i = 0; i < len*2+32; i+=32) {					
			s.append(i/32 + "  :  ");
			if (i > st.length())
				break;
			for (int j = 0; j < 32; j+=2){
				if (i+j+2 > st.length())
					break;
				s.append(st.substring(i+j, i+j+2) + " ");
			}
			s.append("\r\n\r\n");
		}
		os.write(s.toString().getBytes(), 0, s.length());
	}
}
