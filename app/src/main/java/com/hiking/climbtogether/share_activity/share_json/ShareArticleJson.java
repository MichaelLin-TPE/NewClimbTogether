package com.hiking.climbtogether.share_activity.share_json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareArticleJson implements Serializable {
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("email")
    private String email;
    @SerializedName("userPhoto")
    private String userPhoto;
    @SerializedName("content")
    private String content;
    @SerializedName("share_photo")
    private ArrayList<String> sharePhoto;
    @SerializedName("click_member")
    private ArrayList<ShareClickLikeObject> click_member;
    @SerializedName("like")
    private long like;
    @SerializedName("reply")
    private long reply;


    public ArrayList<String> getSharePhoto() {
        return sharePhoto;
    }

    public void setSharePhoto(ArrayList<String> sharePhoto) {
        this.sharePhoto = sharePhoto;
    }

    public ArrayList<ShareClickLikeObject> getClick_member() {
        return click_member;
    }

    public void setClick_member(ArrayList<ShareClickLikeObject> click_member) {
        this.click_member = click_member;
    }



    public long getReply() {
        return reply;
    }

    public void setReply(long reply) {
        this.reply = reply;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }
}
