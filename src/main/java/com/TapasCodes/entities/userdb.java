package com.TapasCodes.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class userdb {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int dbid;
	private String image;
	private String text;
	private String upload;
	@ManyToOne
	@JsonIgnore
	private user user;
	@Override
	public String toString() {
		return "userdb [dbid=" + dbid + ", image=" + image + ", text=" + text + ", upload=" + upload + ", user=" + user
				+ "]";
	}
	public int getDbid() {
		return dbid;
	}
	public void setDbid(int dbid) {
		this.dbid = dbid;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public user getUser() {
		return user;
	}
	public void setUser(user user) {
		this.user = user;
	} 
	
	
}
