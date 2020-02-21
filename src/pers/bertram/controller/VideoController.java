package pers.bertram.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import pers.bertram.beans.FileTreeInfoType;
import pers.bertram.util.FileUtil;
import pers.bertram.util.JSON;
import pers.bertram.util.RuntimeUtil;
import pers.bertram.util.StringUtil;

@Controller
public class VideoController {

	@RequestMapping("/videoplay")
	@ResponseBody
	public String videoPlay(String path) {
		File file = FileUtil.getFile(path);
		if (file == null)
			return "{}";
		if (!FileUtil.getFileType(file).equals(FileTreeInfoType.VIDEO))
			return "{}";
		List<File> fileList = FileUtil.getDirVideoList(file.getParentFile());
		File pref = file;
		File nextf = file;
		for (int i = 0; i < fileList.size(); i++) {
			if (file.equals(fileList.get(i))) {
				if (i+1 < fileList.size())
					nextf = fileList.get(i+1);
				else
					nextf = fileList.get(0);
				if (i == 0)
					pref = fileList.get(fileList.size()-1);
				else if (i > 0)
					pref = fileList.get(i-1);
				break;
			}
		}
		Map mp = new HashMap();
		mp.put("videoname", file.getName());
		mp.put("videopath", StringUtil.encodeURI(file.getPath()));
		mp.put("prevideoname", pref.getName());
		mp.put("prevideopath", StringUtil.encodeURI(pref.getPath()));
		mp.put("nextvideoname", nextf.getName());
		mp.put("nextvideopath", StringUtil.encodeURI(nextf.getPath()));
		return JSON.stringify(mp);
	}
	
	@RequestMapping("videoshortcut")
	//Runtime 执行 ffmpeg 生成视频截图，存放于项目/img/videocut/下
	public String getShortCut(String path, HttpServletRequest req) {
		File file = FileUtil.getFile(path);
		String realPath = file.getParent(); 
		System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径 
		if (file != null) {
			String srcPath = file.getPath();
			String destPath = realPath + "\\videocut\\" + file.getName() + ".jpg";
			File destFile = null;
			try {
				destFile = new File(destPath);
				if (!destFile.getParentFile().exists()) destFile.getParentFile().mkdir();
			} catch(Exception e) {
				return "redirect:./img/notfound.jpg";
			}
			if (destFile == null)
				return "redirect:./img/notfound.jpg";
			if (!destFile.exists()) {
				String cmd = "ffmpeg -ss 00:02:06 -i \"" + srcPath + "\" -f image2 -y \"" + destPath + "\"";
				RuntimeUtil.execCmd(cmd);
				System.out.println("cmd : " + cmd);
			}
			return "redirect:./download?path=" + StringUtil.encodeURI(destFile.getPath());
		}
		return "redirect:./img/notfound.jpg";
	}
}
