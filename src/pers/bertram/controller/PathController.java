package pers.bertram.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pers.bertram.util.FileUtil;
import pers.bertram.util.StringUtil;

@Controller
public class PathController {
	
	
	@RequestMapping("/path")
	public ModelAndView simplePath(String path) {
		ModelAndView mav = new ModelAndView("showpath");
		File root = FileUtil.getFile(path);
		if (root == null)
			return new ModelAndView("showroot");
		
		if (root.isDirectory()) {
			mav.addObject("path", root.getPath());
			//mav.addObject("encodedpath", StringUtil.encodeURI(root.getPath()));
			mav.addObject("value", splitFile(root, StringUtil.encodeURI(root.getPath())));
			mav.addObject("prepath", StringUtil.encodeURI(root.getParent()));
		} else {
			return new ModelAndView("./path?path=" + StringUtil.encodeURI(root.getParent()));
		}
		
		return mav;
	}
	
	private List<Map<String, Object>> splitFile(File root, String path) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		listMap.add(createMap("dir"));
		listMap.add(createMap("pic"));
		listMap.add(createMap("vid"));
		listMap.add(createMap("oth"));
		listMap.add(createMap("aud"));
		File[] fileList = root.listFiles();
		if (fileList == null)
			return listMap;
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			String type = FileUtil.getFileType(file);
			for (Map m : listMap) {
				if (type.equals(m.get("type")))
					((List)m.get("list")).add(createFileInfo(type, file, path));
			}
		}
		for (int i = listMap.size()-1; i >= 0; i--) {
			Map mp = listMap.get(i);
			if (((List)mp.get("list")).size() == 0)
				listMap.remove(mp);
		}
		return listMap;
	}
	
	private Map createFileInfo(String type, File file, String path) {
		Map mp = new HashMap();
		mp.put("name", file.getName());
		mp.put("querystr", "path=" + path + "%5c" + StringUtil.encodeURI((String)mp.get("name")));
		String date = new Date(file.lastModified()).toString();
		mp.put("modi", date.substring(0, date.length()-8));
		if (!file.isDirectory()) {
			double sizel = file.length();
			if (sizel > 0)
				sizel /= 1024;
			String size = String.format("%.0f", sizel) + "KB";
			if (sizel >= 1024) {
				sizel /= 1024;
				size = String.format("%.2f", sizel) + "M";
			}
			if (sizel >= 1024) 
				size = String.format("%.2f", sizel/1024) + "G";
			mp.put("size", size);
		}
		return mp;
	}
	
	private Map createMap(String type) {
		Map mp = new HashMap();
		mp.put("type", type);
		mp.put("list", new ArrayList());
		return mp;
	}
	
}
