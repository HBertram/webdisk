package pers.bertram.beans;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pers.bertram.util.FileUtil;
import pers.bertram.util.JSON;
import pers.bertram.util.StringUtil;

public class FileTree implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileTree(File file) {
		this.path = file.getPath();
		try {
			this.setParentPath(file.getParent());
			this.setParentEncodedPath(StringUtil.encodeURI(file.getParent()));
		} catch (Exception e) {
			
		}
        Field[] fields = FileTree.class.getDeclaredFields();
        
		File[] fileList = file.listFiles();
		
		for (int i = 0; i < fileList.length; i++) {
			File f = fileList[i];
			String type = FileUtil.getFileType(f);
            //得到所有属性

			List<FileTreeInfo> lst = null;
            for (int j = 0; j < fields.length; j++){//遍历
               try {
                   //得到属性
                   Field field = fields[j];
                   //打开私有访问
                   field.setAccessible(true);
                   //获取属性
                   String name = field.getName();
                   //获取属性值
                   if (name.equals(type)) {
                	   lst = (List<FileTreeInfo>) field.get(this);
                	   break;
                   }
               } catch (IllegalAccessException e) {
                   e.printStackTrace();
               }
            }
            lst.add(createFileInfo(f, type));
		}
	}
	

	
    @Override
    public String toString() {
        return JSON.stringify(this);
    }
    
	private FileTreeInfo createFileInfo(File file, String type) {
		FileTreeInfo fti = new FileTreeInfo();

		fti.setName(file.getName());
		fti.setPath(file.getAbsolutePath());
		fti.setEncodedPath(StringUtil.encodeURI(file.getAbsolutePath()));
		fti.setType(type);
		String dateString = new Date(file.lastModified()).toString();
		fti.setLastModifiedTime(dateString.substring(0, dateString.length()-8));
		String size = "";
		if (!file.isDirectory()) {
			double sizel = file.length();
			if (sizel > 0)
				sizel /= 1024;
			size = String.format("%.0f", sizel) + "KB";
			if (sizel >= 1024) {
				sizel /= 1024;
				size = String.format("%.2f", sizel) + "M";
			}
			if (sizel >= 1024) 
				size = String.format("%.2f", sizel/1024) + "G";
			
		}
		fti.setSize(size);
		return fti;
	}
	
	private String path = "";
	private String parentPath;
	private String parentEncodedPath;

	private List<FileTreeInfo> directories = new ArrayList<FileTreeInfo>();
	private List<FileTreeInfo> pictures = new ArrayList<FileTreeInfo>();
	private List<FileTreeInfo> videos = new ArrayList<FileTreeInfo>();
	private List<FileTreeInfo> audios = new ArrayList<FileTreeInfo>();
	private List<FileTreeInfo> others = new ArrayList<FileTreeInfo>();
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	public String getParentEncodedPath() {
		return parentEncodedPath;
	}
	public void setParentEncodedPath(String parentEncodedPath) {
		this.parentEncodedPath = parentEncodedPath;
	}
	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public List<FileTreeInfo> getDirectories() {
		return directories;
	}



	public void setDirectories(List<FileTreeInfo> directories) {
		this.directories = directories;
	}



	public List<FileTreeInfo> getPictures() {
		return pictures;
	}



	public void setPictures(List<FileTreeInfo> pictures) {
		this.pictures = pictures;
	}



	public List<FileTreeInfo> getVideos() {
		return videos;
	}



	public void setVideos(List<FileTreeInfo> videos) {
		this.videos = videos;
	}



	public List<FileTreeInfo> getAudios() {
		return audios;
	}



	public void setAudios(List<FileTreeInfo> audios) {
		this.audios = audios;
	}



	public List<FileTreeInfo> getOthers() {
		return others;
	}



	public void setOthers(List<FileTreeInfo> others) {
		this.others = others;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}