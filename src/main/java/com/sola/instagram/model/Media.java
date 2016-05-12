package com.sola.instagram.model;

/*
Copyright (c) 2013 Sola Ogunsakin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sola.instagram.exception.InstagramException;
import com.sola.instagram.io.GetMethod;
import com.sola.instagram.io.UriFactory;
import com.sola.instagram.util.UriConstructor;

/**
 * Object for a piece of Media
 * with the JSON representation
 * <pre>{
 * 			"attribution":null,
 * 			"tags":["", ""],
 * 			"type":"image",
 * 			"location":null,
 *		    "users_in_photo": [{
 *		        "user": {
 *		            "username": "",
 *		            "full_name": "",
 *		            "id": "",
 *		            "profile_picture": ""
 *		        },
 *		        "position": {
 *		            "x": 0,
 *		            "y": 0
 *		        }
 *		    }], 
 * 			"comments": {
 * 				"count":0,
 * 				"data":[ {
 * 							"username":"",
 * 							"profile_picture":"",
 * 							"id":"",
 * 							"full_name":""
 * 						} ]
 * 			},
 * 			"filter":"",
 * 			"created_time":"",
 * 			"link":"",
 * 			"likes": {
 * 				"count": 0,
 * 				"data":[ {
 * 							"username":"",
 * 							"profile_picture":"",
 * 							"id":"",
 * 							"full_name":""
 * 						} ],
 * 			},
 * 			"images": {
 * 				"low_resolution": {
 * 					"url":"",
 * 					"width":0,
 * 					"height":0
 * 				},
 * 				"thumbnail": {
 * 					"url":"",
 * 					"width":0,
 * 					"height":0
 * 				},
 * 				"standard_resolution":{
 * 					"url":"",
 * 					"width":0,"height":0}
 * 				},
 * 			"caption": { 
 * 				"created_time":"0",
 * 				"text":"",
 * 				"from": {
 * 					"username":"",
 * 					"profile_picture":"",
 * 					"id":"",
 * 					"full_name":""
 * 				},
 * 				"id":""
 * 			},
 * 			"user_has_liked":false,
 * 			"id":"",
 * 			"user": {
 * 				"username":"",
 * 				"website":"",
 * 				"bio":"",
 * 				"profile_picture":"",
 * 				"full_name":"",
 * 				"id":""
 * 			}
 * }</pre>
 * @author Sola Ogunsakin
 * @version 2013-10-13
 */
public class Media extends InstagramModel {

	/** 
	 * Types of image filters
	 */
	public static enum Filters {
		TOASTER, HUDSON, SIERRA, INKWELL, NORMAL, AMARO, RISE, VALENCIA, NONE
	}
	
	/** 
	 * Type of Media
	 */
	protected String type;
	
	/** 
	 * The image filter
	 */
	protected String filter;
	
	/** 
	 * The link for this media
	 */
	protected String link;
	
	/** 
	 * List of tags used in this media
	 */
	protected List<String> tags;

	/** 
	 * Low resolution image version of the media's image
	 */
	protected Image lowResolutionImage;

	/** 
	 * Thumbnail resolution image version of the media's image
	 */
	protected Image thumbnailImage;
	
	/**
	 * Standard resolution version of the media's image
	 */
	protected Image standardResolutionImage;
	
	/**
	 * List of lazyloaded Comment objects for this media 
	 */
	protected List<Comment> comments;
	
	/**
	 * List of lazyloaded User objects for users who
	 * liked the media
	 */
	protected List<User> likers;
	
	/**
	 * User who created this media
	 */
	protected User user;
	
	/**
	 * Location object representing where this media was created
	 */
	protected Location location = null;
	
	/**
	 * The creation timestamp as a string
	 */
	protected String createdTimestamp;
	
	/**
	 * Id of this media as a string
	 */
	protected String id;
	
	/**
	 * Caption object representing this media's caption
	 */
	protected Caption caption;
	
	/**
	 * Boolean indicating if the current user has liked this media
	 */
	protected Boolean userHasLikedMedia;

	/**
	 * Users tagged in photo
	 */
	protected List<UserPhotoTag> usersInPhoto;

	/**
	 * Number of likes for this media
	 */
	protected int likeCount;
	
	
	/**
	 * Number of comments for this media
	 */
	protected int commentCount;
	
	/**
	 * Used to construct formatted api urls
	 */
	UriConstructor uriConstructor;
	
    /**
     * Makes a new Media object out of a JSONObject
     * @param obj json object used to create this media
     * @param accessToken API access token used for lazyloaded api requests
     * @throws JSONException
     */
	public Media(JSONObject obj, String accessToken) throws JSONException {
		super(obj, accessToken);
		if(!obj.isNull("caption")) 
			setCaption(this.new Caption(obj.getJSONObject("caption")));			
		setCreatedTimestamp(obj.getString("created_time"));
		setFilter(obj.optString("filter"));
		setLink(obj.optString("link"));
		setId(obj.getString("id"));
		setType(obj.getString("type"));
		setUser(new User(obj.getJSONObject("user"), accessToken));
		setUserHasLikedMedia(obj.getBoolean("user_has_liked"));
		
	 	JSONObject images = obj.getJSONObject("images");
	 	setLowResolutionImage(this.new Image(images.getJSONObject("low_resolution")));
	 	setThumbnailImage(this.new Image(images.getJSONObject("thumbnail")));
	 	setStandardResolutionImage(this.new Image(images.getJSONObject("standard_resolution")));
		
		if(!obj.isNull("location")) {
			setLocation(new Location(obj.getJSONObject("location"), accessToken));
		}
		
		
		JSONArray tagStrings   = obj.getJSONArray("tags");
		ArrayList<String> tags = new ArrayList<String>();
		for(int i = 0; i < tagStrings.length(); i++) {
			tags.add(tagStrings.getString(i));
		}
		setTags(tags);

		
		JSONArray jsonUserPhotoTags = obj.optJSONArray("users_in_photo");
		ArrayList<UserPhotoTag> userPhotoTags = new ArrayList<UserPhotoTag>();
		if(jsonUserPhotoTags != null) {
			for(int i = 0; i < jsonUserPhotoTags.length(); i++) {
				userPhotoTags.add(
					new UserPhotoTag(jsonUserPhotoTags.getJSONObject(i))
				);
			}
		} 
		setUsersInPhoto(userPhotoTags);
		
		JSONObject likes = obj.getJSONObject("likes");
		setLikeCount(likes.getInt("count"));
		
		JSONObject comments = obj.getJSONObject("comments");
		setCommentCount(comments.getInt("count"));		
		uriConstructor = new UriConstructor(getAccessToken());
	}
	
	
	public static Media fromJSON(JSONObject obj, String accessToken) throws JSONException {
		if(obj.getString("type").equals("video")) {
			return new VideoMedia(obj, accessToken);
		} else {
			return new ImageMedia(obj, accessToken);
		}
	}
	
    /**
     * Returns the type of this media
     * @return The type of this media 
     */
	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

    /**
     * Returns the type filter for this media's image
     * @return The type filter for this media's image 
     */
	public String getFilter() {
		return filter;
	}


	protected void setFilter(String filter) {
		this.filter = filter;
	}

    /**
     * Returns the number of likes for this media
     * @return The number of likes for this media 
     */
	public int getLikeCount() {
		return likeCount;
	}

	private void setLikeCount(int count) {
		likeCount = count;
	}	
	
    /**
     * Returns the number of comments for this media
     * @return The number of comments for this media 
     */
	public int getCommentCount() {
		return commentCount;
	}
	

	private void setCommentCount(int count) {
		commentCount = count;
	}	
	
    /**
     * Returns the Caption object representing this media's caption
     * @return The Caption object representing this media's caption 
     */
	public Caption getCaption() {
		return caption;
	}

    /**
     * Returns the url link to this media
     * @return The url link to this media 
     */
	public String getLink() {
		return link;
	}


	protected void setLink(String link) {
		this.link = link;
	}

    /**
     * Lazy-loads and returns a list of comments for this media
     * @return A list of lazy-loaded comments for this media 
     */
	public List<Comment> getComments() throws Exception {
		if(comments == null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("media_id", getId());
			String uri = uriConstructor.constructUri(UriFactory.Comments.GET_MEDIA_COMMENTS, map, true);
			JSONObject object = (new GetMethod(uri)).call().getJSON();
			JSONArray commentObjects    = object.getJSONArray("data");
			ArrayList<Comment> comments = new ArrayList<Comment>();
			for(int i = 0; i < commentObjects.length(); i++) {
				comments.add(new Comment(commentObjects.getJSONObject(i), accessToken));
			}
			setComments(comments);	
		}
		return comments;
	}
	
	protected void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
    /**
     * Returns the User object of this media's creator
     * @return The User object of this media's creator 
     */
	public User getUser() {
		return user;
	}


	protected void setUser(User user) {
		this.user = user;
	}

    /**
     * Returns the Location object representing location that this 
     * media was created
     * @return Location object representing location that this 
     * media was created 
     */
	public Location getLocation() {
		return location;
	}


	protected void setLocation(Location location) {
		this.location = location;
	}

    /**
     * Indicated whether the current user has liked this media
     * @return a boolean indicating whether the current user 
     * has liked this media
     */
	public Boolean userHasLikedMedia() {
		return userHasLikedMedia;
	}

	protected void setUserHasLikedMedia(Boolean userHasLikedMedia) {
		this.userHasLikedMedia = userHasLikedMedia;
	}
	
    /**
     * Returns this media's creation timestamp a string
     * @return This media's creation timestamp a string
     */
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}


	protected void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

    /**
     * Returns the id of this media
     * @return The id of this media
     */
	public String getId() {
		return id;
	}


	protected void setId(String id) {
		this.id = id;
	}

    /**
     * Returns the low resolution image for this media
     * @return The low resolution image for this media
     */
	public Image getLowResolutionImage() {
		return lowResolutionImage;
	}


	protected void setLowResolutionImage(Image lowResolutionImage) {
		this.lowResolutionImage = lowResolutionImage;
	}

    /**
     * Returns the thumbnail image for this media
     * @return The thumbnail image for this media
     */
	public Image getThumbnailImage() {
		return thumbnailImage;
	}


	protected void setThumbnailImage(Image thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

    /**
     * Returns the standard resolution image for this media
     * @return The standard resolution image for this media
     */
	public Image getStandardResolutionImage() {
		return standardResolutionImage;
	}


	protected void setStandardResolutionImage(Image standardResolutionImage) {
		this.standardResolutionImage = standardResolutionImage;
	}


	protected void setCaption(Caption caption) {
		this.caption = caption;
	}
	
    /**
     * Lazy-Loads and returns a list of users who have liked this media
     * @return A lazy-loaded list of users who have liked this media
     */
	public List<User> getLikers() throws Exception {
		if(likers == null) {
			try {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("media_id", getId());
				String uri = uriConstructor.constructUri(UriFactory.Likes.GET_LIKERS, map, true);
				JSONObject object      = (new GetMethod(uri)).call().getJSON();
				ArrayList<User> likers =  new ArrayList<User>();
				JSONArray likerUserObjects = object.getJSONArray("data");
				for(int i = 0; i < likerUserObjects.length(); i++) {
					likers.add(new User(likerUserObjects.getJSONObject(i), accessToken));
				}
				setLikers(likers);
				
			} catch(JSONException e) {
				throw new InstagramException("JSON parsing error");
			}
		}
		return likers;
	}

	protected void setLikers(List<User> likers) {
		this.likers = likers;
	}

    /**
     * Returns a list of tags (as strings) used in this media
     * @return A list of tags (as strings) used in this media
     */
	public List<String> getTags() {
		return tags;
	}

	protected void setTags(List<String> tags) {
		this.tags = tags;
	}

    /**
     * Returns a list of tags (as strings) used in this media
     * @return A list of tags (as strings) used in this media
     */
	public List<UserPhotoTag> getUsersInPhoto() {
		return this.usersInPhoto;
	}

	protected void setUsersInPhoto(List<UserPhotoTag> users) {
		this.usersInPhoto = users;
	}
	
    /**
     * Checks if two media objects are equal
     * @param o The object to be compared 
     * @return True of the two objects are equal, false otherwise
     */
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(o.getClass() != this.getClass()) return false;
		return ((Media)o).getId().equals(getId());
	}
	
	/**
	 * Object for a media image
	 * with the JSON representation
	 * <pre>
	 * 		{
	 * 			"url":"",
	 * 			"width":0,
	 * 			"height":0
	 * 		}
	 * </pre>
	 * @author Sola Ogunsakin
	 * @version 2012-08-22
	 */
	public class Image {
		/**
		 * Link to this image
		 */
		String uri;
		
		/**
		 * Width of this image
		 */
		int width;
		
		/**
		 * Height of this image
		 */
		int heigth;
	
	    /**
	     * Makes a new Image object out of a JSONObject
	     * @param obj json object used to create this image
	     * @throws JSONException
	     */
		public Image(JSONObject obj) throws JSONException {
			this.setUri(obj.getString("url"));
			this.setWidth(obj.getInt("width"));
			this.setHeigth(obj.getInt("height"));
		}
		
	    /**
	     * Returns the url link to this image
	     * @return The url link to this image 
	     */
		public String getUri() {
			return uri;
		}

	    /**
	     * Sets this image's url
	     * @param url url for this image 
	     */
		protected void setUri(String uri) {
			this.uri = uri;
		}
	
	    /**
	     * Returns the width of this image
	     * @return The width of this image 
	     */
		public int getWidth() {
			return width;
		}
		
	    /**
	     * Sets this image's width
	     * @param width width of this image 
	     */
		protected void setWidth(int width) {
			this.width = width;
		}
		
	    /**
	     * Returns the height of this image
	     * @return The height of this image 
	     */
		public int getHeigth() {
			return heigth;
		}
		
	    /**
	     * Sets this image's height
	     * @param height height of this image 
	     */
		protected void setHeigth(int heigth) {
			this.heigth = heigth;
		}

	    /**
	     * Checks if two image objects are equal
	     * @param o The object to be compared 
	     * @return True of the two objects are equal, false otherwise
	     */
		public boolean equals(Object o) {
			if(o == null) return false;
			if(o == this) return true;
			if(o.getClass() != this.getClass()) return false;
			return ((Media.Image)o).getUri().equals(getUri());
		}
	}
	
	/**
	 * Object for a media caption
	 * with the JSON representation
	 * <pre>
	 *	{ 
	 * 		"created_time":"",
	 * 		"text":"",
	 * 		"from": {
	 * 			"username":"",
	 * 			"profile_picture":"",
	 * 			"id":"",
	 * 			"full_name":""
	 * 		},
	 * 		"id":""
	 * 	}
	 * </pre>
	 * @author Sola Ogunsakin
	 * @version 2012-08-22
	 */
	public class Caption {

		/**
		 * Caption text
		 */
		String text;
		
		/**
		 * Caption's creation timestamp as a string
		 */
		String createdTimestamp;
		
		/**
		 * User object representing the caption's creator
		 */
		User from;
		
		/**
		 * Caption ID as a string
		 */
		String id;
	
	    /**
	     * Makes a new caption object out of a JSONObject
	     * @param captionObject json object used to create this caption
	     * @throws JSONException
	     */
		public Caption(JSONObject captionObject) throws JSONException {
			this.setId(captionObject.getString("id"));
			this.setFrom(new User(captionObject.getJSONObject("from"), accessToken));
			this.setText(captionObject.getString("text"));
			this.setCreatedTimestamp(captionObject.getString("created_time"));
		}
		
	    /**
	     * Returns the text for this caption
	     * @return The text for this caption 
	     */
		public String getText() {
			return text;
		}

	    /**
	     * Sets the text for this caption
	     * @param text the text for this caption 
	     */	
		protected void setText(String text) {
			this.text = text;
		}

	    /**
	     * Returns the caption's creation timestamp as a string
	     * @return The caption's creation timestamp as a string 
	     */
		public String getCreatedTimestamp() {
			return createdTimestamp;
		}

	    /**
	     * Sets the creation timestamp for this caption
	     * @param createdTimestamp the string representation of the creation timestamp for this caption 
	     */	
		protected void setCreatedTimestamp(String createdTimestamp) {
			this.createdTimestamp = createdTimestamp;
		}

	    /**
	     * Returns the creator of this caption
	     * @return The creator of this caption 
	     */
		public User getFrom() {
			return from;
		}

	    /**
	     * Sets creator of this caption
	     * @param from The user object representing the creator of the caption 
	     */	
		protected void setFrom(User from) {
			this.from = from;
		}

	    /**
	     * Returns id of this caption
	     * @return The id of this caption 
	     */
		public String getId() {
			return id;
		}

	    /**
	     * Sets creator of this caption
	     * @param from The user object representing the creator of the caption 
	     */
		protected void setId(String id) {
			this.id = id;
		}		
		
	    /**
	     * Checks if two caption objects are equal
	     * @param o The object to be compared 
	     * @return True of the two objects are equal, false otherwise
	     */
		public boolean equals(Object o) {
			if(o == null) return false;
			if(o == this) return true;
			if(o.getClass() != this.getClass()) return false;
			return ((Media.Caption)o).getId().equals(getId());
		}		
	}

	/**
	 * Object for a user photo tag
	 * with the JSON representation
	 * <pre>
	 *	 {
     *       "user": {
     *           "username": "kevin",
     *           "full_name": "Kevin S",
     *           "id": "3",
     *           "profile_picture": "..."
     *       },
     *       "position": {
     *           "x": 0.315,
     *           "y": 0.9111
     *       }
     *   }
	 * </pre>
	 * @author Sola Ogunsakin
	 * @version 2012-08-22
	 */
	public class UserPhotoTag {
		double x;
		double y;
		User user;

	    /**
	     * Makes a new UserPhotoTag object out of a JSONObject
	     * @param obj json representation of the tag
	     * @throws JSONException
	     */
		public UserPhotoTag(JSONObject obj) throws JSONException {
			JSONObject position = obj.getJSONObject("position");
			this.setX(position.getDouble("x"));
			this.setY(position.getDouble("y"));
			this.setUser(new User(obj.getJSONObject("user"), accessToken));			
		}
		
		public double getX() {
			return x;
		}
		
		private void setX(double x) {
			this.x = x;
		}
		public double getY() {
			return y;
		}
		
		private void setY(double y) {
			this.y = y;
		}
		public User getUser() {
			return user;
		}
		
		private void setUser(User user) {
			this.user = user;
		}
	}
}
