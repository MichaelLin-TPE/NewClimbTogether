package com.example.climbtogether.share_activity;

import java.util.ArrayList;

public class LikeMemberDTO {

    private String content;

    private ArrayList<String> memberEmail;

    private ArrayList<Boolean> isCheckArray;

    public ArrayList<Boolean> getIsCheckArray() {
        return isCheckArray;
    }

    public void setIsCheckArray(ArrayList<Boolean> isCheckArray) {
        this.isCheckArray = isCheckArray;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(ArrayList<String> memberEmail) {
        this.memberEmail = memberEmail;
    }
}
