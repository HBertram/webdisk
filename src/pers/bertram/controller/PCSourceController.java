package pers.bertram.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.bertram.util.FileUtil;
import pers.bertram.util.StringUtil;

public class PCSourceController extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PCSourceController() {
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
		String path = request.getParameter("path");
		path = StringUtil.codeTransfer(path);
		
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
			response.sendRedirect("./path?path=" + StringUtil.encodeURI(file.getPath()));
			return;
		} else if (type.equals("pic")) {
			response.setContentType("image/jpeg");
		} else if (type.equals("oth")) {
			//文件过大则下载 3M
			if (file.length() >= 1024*1024*10) {
				response.sendRedirect("./download?path=" + StringUtil.encodeURI(file.getPath()));
				return;
			}
			//小则以文本形式返回
			response.setContentType("text/html");
		} else if (type.equals("vid")) {
			
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
