package com.sola.instagram.model;

import org.json.JSONObject;
import org.json.JSONException;

public class Comment extends InstagramModel {
	String createdTimestamp;
	String text;
	User sender;
	String id;

	public Comment(JSONObject obj, String accessToken)
			throws JSONException {
		super(obj, accessToken);
		setCreatedTimestamp(obj.getString("created_time"));
		setText(obj.getString("text"));
		setId(obj.getString("id"));
		setSender((new User(obj.getJSONObject("from"), accessToken)));
	}

	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	protected void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getText() {
		return text;
	}

	protected void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	protected void setSender(User sender) {
		this.sender = sender;
	}

    /**
     * Checks if two comment objects are equal
     * @param o The object to be compared 
     * @return True of the two objects are equal, false otherwise
     */
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(o.getClass() != this.getClass()) return false;
		return ((Comment)o).getId().equals(getId());
	}
}