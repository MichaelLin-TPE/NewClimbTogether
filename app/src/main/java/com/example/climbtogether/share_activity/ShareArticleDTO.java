package com.example.climbtogether.share_activity;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareArticleDTO implements Serializable {

    private String displayName;

    private String email;

    private String userPhoto;

    private String selectPhoto;

    private String selectPhoto1;
    private String selectPhoto2;

    private String content;

    private long like;

    private long reply;

    public long getReply() {
        return reply;
    }

    public void setReply(long reply) {
        this.reply = reply;
    }

    public String getSelectPhoto1() {
        return selectPhoto1;
    }

    public void setSelectPhoto1(String selectPhoto1) {
        this.selectPhoto1 = selectPhoto1;
    }

    public String getSelectPhoto2() {
        return selectPhoto2;
    }

    public void setSelectPhoto2(String selectPhoto2) {
        this.selectPhoto2 = selectPhoto2;
    }

    private ArrayList<ReplyDTO> replyArray;

    public ArrayList<ReplyDTO> getReplyArray() {
        return replyArray;
    }

    public void setReplyArray(ArrayList<ReplyDTO> replyArray) {
        this.replyArray = replyArray;
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

    public String getSelectPhoto() {
        return selectPhoto;
    }

    public void setSelectPhoto(String selectPhoto) {
        this.selectPhoto = selectPhoto;
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
