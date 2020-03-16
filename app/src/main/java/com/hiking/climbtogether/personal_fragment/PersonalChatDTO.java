package com.hiking.climbtogether.personal_fragment;

import android.content.ContentValues;
import android.database.Cursor;

public class PersonalChatDTO {

    private int sid;

    private String photoUrl;

    private String friendEmail;

    private String displayName;

    private String message;

    private long time;

    private String documentPath;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void fromCursor(Cursor cursor){
        sid = cursor.getInt(cursor.getColumnIndex("sid"));
        photoUrl = cursor.getString(cursor.getColumnIndex("photo_url"));
        friendEmail = cursor.getString(cursor.getColumnIndex("friend_email"));
        displayName = cursor.getString(cursor.getColumnIndex("display_name"));
        message = cursor.getString(cursor.getColumnIndex("message"));
        time = cursor.getInt(cursor.getColumnIndex("time"));
        documentPath = cursor.getString(cursor.getColumnIndex("document_path"));
    }

    public ContentValues toContentValues(){
        ContentValues data = new ContentValues();
        data.put("photo_url",photoUrl);
        data.put("friend_email",friendEmail);
        data.put("display_name",displayName);
        data.put("message",message);
        data.put("time",time);
        data.put("document_path",documentPath);
        return data;
    }
}
