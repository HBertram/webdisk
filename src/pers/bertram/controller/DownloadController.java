package pers.bertram.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.bertram.util.FileUtil;
import pers.bertram.util.StringUtil;

public class DownloadController extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DownloadController() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		try {
//			System.out.close();
//			System.setOut(new PrintStream(new FileOutputStream(new File("e:\\1.txt"), true)));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String path = request.getParameter("path");
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

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		
	}

}
