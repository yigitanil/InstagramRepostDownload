package com.izmit.instarepost.model;

public class Instagram {
	
	private String id;
	private String url;
	private String username;
	private String thumb;
	private String format;
	private String userThumb;


	public Instagram() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}


	public String getUserThumb() {
		return userThumb;
	}

	public void setUserThumb(String userThumb) {
		this.userThumb = userThumb;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
