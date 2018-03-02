package pers.bertram.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import com.sun.org.apache.xml.internal.serialize.Printer;

public class RuntimeUtil {
	public static boolean execCmd(String cmd) {
		cmd = "cmd.exe /c " + cmd;
		Runtime runtime = Runtime.getRuntime();
		Process p = null;
		try {
			p = runtime.exec(cmd);
			//p.waitFor();
			OutputStream out = new ByteArrayOutputStream();
			InputStream in = p.getErrorStream();
			byte[] b = new byte[512];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				//out.write(b, 0, len);
				//System.out.println(new String(b));
				//out.write(b, 0, len);
			}
			//out.flush();
			//String s = out.toString();
			//byte[] bb = s.getBytes();
			//StringUtil.logFile(bb, bb.length, new File("E:\\1.txt"));
			//System.out.println(new String(bb, "GB2312").toString());
			//out.flush();
			out.close();
			in.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		//execCmd("cmd /c dir E:\\");
		execCmd("D:\\ffmpeg-20180112-1eb7c1d-win32-static\\bin\\ffmpeg -ss 00:02:06 -i e:\\dest.mp4 -f image2 -y e:\\1.jpg");
	}
}
