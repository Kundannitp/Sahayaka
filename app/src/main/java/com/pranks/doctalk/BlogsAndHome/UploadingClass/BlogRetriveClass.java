package com.pranks.doctalk.BlogsAndHome.UploadingClass;

public class BlogRetriveClass {
    String profilepic,name,blogdata,mail,uid;
    int likes;
    long noofcomments;
    boolean thumbcolor;
    public long timestamp;

    public BlogRetriveClass(String profilepic, String name, String blogdata, String mail, int likes, long noofcomments, boolean thumbcolor, long timestamp,String uid) {
        this.profilepic = profilepic;
        this.name = name;
        this.blogdata = blogdata;
        this.mail = mail;
        this.likes = likes;
        this.noofcomments = noofcomments;
        this.thumbcolor = thumbcolor;
        this.timestamp = timestamp;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlogdata() {
        return blogdata;
    }

    public void setBlogdata(String blogdata) {
        this.blogdata = blogdata;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getNoofcomments() {
        return noofcomments;
    }

    public void setNoofcomments(int noofcomments) {
        this.noofcomments = noofcomments;
    }

    public boolean isThumbcolor() {
        return thumbcolor;
    }

    public void setThumbcolor(boolean thumbcolor) {
        this.thumbcolor = thumbcolor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
