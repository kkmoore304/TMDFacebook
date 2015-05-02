package com.tellmedoctor.tmdfacebook.model;

/**
 * Created by kmoore on 5/2/15.
 */
public class questionItem {
    private String user_id;
    private String post_txt; //pos_
    private String ans_to_post_id;

    public questionItem(String user_id, String post_txt, String ans_to_post_id) {
        this.user_id = user_id;
        this.post_txt = post_txt;
        this.ans_to_post_id = ans_to_post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_txt() {
        return post_txt;
    }

    public void setPost_txt(String post_txt) {
        this.post_txt = post_txt;
    }

    public String getAns_to_post_id() {
        return ans_to_post_id;
    }

    public void setAns_to_post_id(String ans_to_post_id) {
        this.ans_to_post_id = ans_to_post_id;
    }
}
