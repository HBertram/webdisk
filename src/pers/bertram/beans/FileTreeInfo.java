package pers.bertram.beans;

public class FileTreeInfo {
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEncodedPath() {
		return encodedPath;
	}
	public void setEncodedPath(String encodedPath) {
		this.encodedPath = encodedPath;
	}


	private String path;
	private String encodedPath;

	private String type;
	private String size;
	private String name;
	private String lastModifiedTime;
}
