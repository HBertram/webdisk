package pers.bertram.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

import pers.bertram.beans.FileTree;
import pers.bertram.util.FileUtil;
import pers.bertram.util.JSON;
import pers.bertram.util.StringUtil;

@Controller
public class PathController {
	
	
	@RequestMapping("/path")
	@ResponseBody
	public String simplePath(String path) throws JsonProcessingException {
		ModelAndView mav = new ModelAndView("showpath");
		File root = FileUtil.getFile(path);

		if (root.isDirectory()) {
			mav.addObject("path", root.getPath());
			//mav.addObject("encodedpath", StringUtil.encodeURI(root.getPath()));
			//mav.addObject("value", splitFile(root, StringUtil.encodeURI(root.getPath())));
			mav.addObject("prepath", StringUtil.encodeURI(root.getParent()));
		} else {
			//return new ModelAndView("./path?path=" + StringUtil.encodeURI(root.getParent()));
			return null;
		}
		
		return new FileTree(root).toString();
	}
	
	@RequestMapping("/source")
	public void source(String path, HttpServletResponse response) throws IOException {

		File file;
		try {
			file = new File(path);
		} catch (Exception e) {
			response.sendError(404); return;
		}
		if (file == null || !file.exists()) {
			response.sendError(404); return; 
		}
		
		String type = FileUtil.getFileType(file);
		
		//设置content-type
		if (type.equals("dir")) {
			response.sendRedirect("./showpath.html?path=" + StringUtil.encodeURI(file.getPath()));
			return;
		} else if (type.equals("pictures")) {
			response.setContentType("image/jpeg");
		} else if (type.equals("others")) {
			//文件过大则下载 3M
			if (file.length() >= 1024*1024*10) {
				response.sendRedirect("./download?path=" + StringUtil.encodeURI(file.getPath()));
				return;
			}
			//小则以文本形式返回
			response.setContentType("text/html");
		} else if (type.equals("videos")) {
			
		}
		
		response.setContentLength((int)file.length());
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(response.getOutputStream());
			int len = 0;
			byte[] b = new byte[1024];
			while ((len=in.read(b)) > 0) {
				out.write(b, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in!=null) try {in.close(); }catch(Exception e){}
			if (out!=null) try {out.close(); }catch(Exception e){}
		}
		return;
	}
	
	@RequestMapping("/download")
	public void source(String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Enumeration headers = request.getHeaderNames();
		System.out.println("request:");
		while (headers.hasMoreElements()) {
			String header = (String)headers.nextElement();
			System.out.println(header + " : " + request.getHeader(header));
		}
		System.out.println();
		System.out.println();
		
		File file = FileUtil.getFile(path);
		if (file == null){
			response.sendError(404); return; 
		}
			
		if (file.isDirectory()){
			
		} else if (file.isFile()) {
			long fileSize = file.length();
			String fileETag = Integer.toString(file.hashCode());
			long begin = 0;
			long end = fileSize - 1;
			String range = request.getHeader("range");
			
			String etag = request.getHeader("If-Match");
			if (etag != null && !etag.equals(fileETag)) {
				range = null;
			}
			
			if (range != null && range.startsWith("bytes=")) {
				range = range.substring(6);
				String[] ranges = range.split("-");
				begin = Integer.parseInt(ranges[0]);
				if (ranges.length > 1)
					end = Integer.parseInt(ranges[1]);
				
				response.setHeader("Content-Range", "bytes " + begin + "-" + end + "/" + fileSize);
				response.setContentLength((int)(end-begin+1));
				response.setHeader("Content-type", "application/octet-stream");
				if ( begin == 0 && end == fileSize)
					response.setStatus(200);
				else
					response.setStatus(206);

				//String connection = "";
				//if ((connection = request.getHeader("connection"))!= null && connection.equals("close") && end - begin > 1 * 1024 * 1024 && begin + 1 * 1024 * 1024 <= fileSize) 
				//	end = begin + 1 * 1024 * 1024;
				
				
			} else {
				response.setStatus(200);
				response.setContentType("application/octet-stream");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + StringUtil.encodeURI(file.getName()));// 设置文件名
				response.setContentLength((int)fileSize);
			}
			
			response.setHeader("Last-Modified", new Date(file.lastModified()).toString());
			response.setHeader("ETag", fileETag);
			
			
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			int pos = (int)begin;
			int len = 0;
			try {
				in = new BufferedInputStream(new FileInputStream(file));
				out = new BufferedOutputStream(response.getOutputStream());
				in.skip(begin);
				int flush = 0;
				byte[] b = new byte[1024];
				while ((len=in.read(b)) > 0) {
					pos += len;
					if (pos <= end)
						out.write(b, 0, len);
					else {
						pos -= len;
						out.write(b, 0, (int)(end-pos+1));
						System.out.println("传输完成 begin:" + begin + " end:" + end + " pos:" + pos + " len:" + len);
						break;
					}
					flush++;
					if (flush >= 5) {
						out.flush();
						flush = 0;
					}
				}
				out.flush();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("begin:" + begin + " end:" + end + " pos:" + pos + " len:" + len);
				System.out.println();
				//e.printStackTrace();
				//System.out.println("download interrupted.");
			} finally {
				if (in!=null) try {in.close(); }catch(Exception e){}
				if (out!=null) try {out.close(); }catch(Exception e){}
			}
			return;
		}
	}
}
