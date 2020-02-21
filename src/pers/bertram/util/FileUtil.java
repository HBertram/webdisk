package pers.bertram.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	private static final String[] picSuf = new String[]{"jpg", "png"};
	private static final String[] vidSuf = new String[]{"mp4", "mkv", "rmvb", "avi"};
	private static final String[] audSuf = new String[]{"mp3", "ogg", "wav", "aac"};
	
	public static String getFileType(File file) {
		if (file == null)
			return "others";
		if (file.isDirectory())
			return "directories";
		String suf = getFileSuffix(file);
		for (int i = 0; i < picSuf.length; i++) {
			if (suf.equals(picSuf[i]))
				return "pictures";
		}
		for (int i = 0; i < vidSuf.length; i++) {
			if (suf.equals(vidSuf[i]))
				return "videos";
		}	
		for (int i = 0; i < audSuf.length; i++) {
			if (suf.equals(audSuf[i]))
				return "audios";
		}
		return "others";
	}
	
	public static String getFileSuffix(File file) {
		String name = file.getName();
		return name.substring(name.lastIndexOf(".")+1).toLowerCase();
	}
	
	public static List<File> getDirVideoList(File file) {
		List list = new ArrayList();
		if (file == null || !file.isDirectory())
			return list;
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File subF = fileList[i];
			if (getFileType(subF).equals("vid")) {
				list.add(subF);
			}
		}
		return list;
	}
	
	public static File getFile(String path) {
		File f = null;
		if (path == null || path.trim().equals("")) {
			return f;
		}
		try {
			f = new File(path);
		} catch (Exception e) { e.printStackTrace(); }
		if (f == null || !f.exists())
			f = null;
		return f;
	}
	
	public static String getFileHashCode(File file) {
		return Integer.toString(file.hashCode());
	}
}
