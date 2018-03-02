package pers.bertram.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pers.bertram.util.FileUtil;
import pers.bertram.util.RuntimeUtil;
import pers.bertram.util.StringUtil;

@Controller
public class VideoController {

	@RequestMapping("/videoplay")
	public ModelAndView videoPlay(String path) {
		ModelAndView mav = new ModelAndView();
		File file = FileUtil.getFile(path);
		if (file == null)
			return new ModelAndView("showroot");
		if (!FileUtil.getFileType(file).equals("vid"))
			return new ModelAndView("redirect:./path?path="+StringUtil.encodeURI(path));
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
		mav.addObject("videoname", file.getName());
		mav.addObject("videopath", "./download?path=" + StringUtil.encodeURI(file.getPath()));
		mav.addObject("prevideoname", pref.getName());
		mav.addObject("prevideopath", "./videoplay?path=" + StringUtil.encodeURI(pref.getPath()));
		mav.addObject("nextvideoname", nextf.getName());
		mav.addObject("nextvideopath", "./videoplay?path=" + StringUtil.encodeURI(nextf.getPath()));
		mav.setViewName("playvideo5");
		return mav;
	}
	
	@RequestMapping("videoshortcut")
	//Runtime 执行 ffmpeg 生成视频截图，存放于项目/img/videocut/下
	public String getShortCut(String path, HttpServletRequest req) {
		String realPath = req.getRealPath("/"); 
		File file = FileUtil.getFile(path);
		if (file != null) {
			String srcPath = file.getPath();
			String destPath = realPath + "\\img\\videocut\\" + FileUtil.getFileHashCode(file) + ".jpg";
			File destFile = null;
			try {
				destFile = new File(destPath);
			} catch(Exception e) {
				return "redirect:./img/notfound.jpg";
			}
			if (destFile == null)
				return "redirect:./img/notfound.jpg";
			if (!destFile.exists()) {
				String cmd = realPath + "runtime\\ffmpeg -ss 00:02:06 -i \"" + srcPath + "\" -f image2 -y \"" + destPath + "\"";
				RuntimeUtil.execCmd(cmd);
				System.out.println("cmd : " + cmd);
			}
			return "redirect:./img/videocut/" + destFile.getName();
		}
		return "redirect:./img/notfound.jpg";
	}
}
