package pers.bertram.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import pers.bertram.util.FileUtil;
import pers.bertram.util.StringUtil;


@Controller
public class FileUpload {
	
	@RequestMapping(value = "/upload", method=RequestMethod.POST)
	public String getUploadFile(@RequestParam("file") CommonsMultipartFile file, String path) {
		path = StringUtil.codeTransfer(path);
		File target = new File(path + "\\" + file.getOriginalFilename());
		System.out.println("upload to " + path);
		if (target != null) {
			try {
				file.transferTo(target);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "redirect:./path?path=" + StringUtil.encodeURI(path);
	}
}
