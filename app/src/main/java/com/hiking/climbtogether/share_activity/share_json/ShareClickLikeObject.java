package com.hiking.climbtogether.share_activity.share_json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShareClickLikeObject implements Serializable {
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("photo_url")
    private String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
}
