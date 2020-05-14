package com.pranks.doctalk.BlogsAndHome.UploadingClass;

public class BlogUploadClass {
    String profilepic,name,blogdata,mail,key,uidsort,keysort,havetoshow;
    int likes;
    public long timestamp;

    public String getHavetoshow() {
        return havetoshow;
    }

    public void setHavetoshow(String havetoshow) {
        this.havetoshow = havetoshow;
    }

    public BlogUploadClass(String profilepic, String name, String blogdata, String mail,
                           int likes, long timestamp, String uidsort, String keysort) {
        this.profilepic = profilepic;
        this.name = name;
        this.blogdata = blogdata;
        this.mail = mail;
        this.uidsort = uidsort;
        this.keysort = keysort;
        this.likes = likes;
        this.timestamp = timestamp;
    }

    public String getUidsort() {
        return uidsort;
    }

    public void setUidsort(String uidsort) {
        this.uidsort = uidsort;
    }

    public String getKeysort() {
        return keysort;
    }

    public void setKeysort(String keysort) {
        this.keysort = keysort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public BlogUploadClass(String profilepic, String name, String blogdata, String mail, int likes,long timestamp,String havetoshow) {
        this.profilepic = profilepic;
        this.name = name;
        this.blogdata = blogdata;
        this.likes=likes;
        this.mail=mail;
        this.timestamp=timestamp;
        this.havetoshow=havetoshow;
    }



    public BlogUploadClass(String blogdata,int likes,long timestamp,String key,String havetoshow) {
        this.blogdata = blogdata;
        this.likes=likes;
        this.timestamp=timestamp;
        this.key=key;
        this.havetoshow=havetoshow;
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
}
