package com.hiking.climbtogether.share_activity;

import java.util.ArrayList;

public class ReplyObject {

    private String articleName;

    private ArrayList<ReplyDTO> replyArray;

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public ArrayList<ReplyDTO> getReplyArray() {
        return replyArray;
    }

    public void setReplyArray(ArrayList<ReplyDTO> replyArray) {
        this.replyArray = replyArray;
    }
}
