package com.example.climbtogether.share_activity;

public class ShareArticleDTO {
    private String diaplayName;

    private String email;

    private String userPhoto;

    private String selectPhoto;

    private String content;

    private Long like;

    public String getDiaplayName() {
        return diaplayName;
    }

    public void setDiaplayName(String diaplayName) {
        this.diaplayName = diaplayName;
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

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }
}
