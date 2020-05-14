package com.pranks.doctalk.BlogsAndHome.UploadingClass;

public class CommentClass {
    String imgurl,comment_stm,name_of_person;

    public CommentClass(String imgurl, String comment_stm, String name_of_person) {
        this.imgurl = imgurl;
        this.comment_stm = comment_stm;
        this.name_of_person = name_of_person;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getComment_stm() {
        return comment_stm;
    }

    public void setComment_stm(String comment_stm) {
        this.comment_stm = comment_stm;
    }

    public String getName_of_person() {
        return name_of_person;
    }

    public void setName_of_person(String name_of_person) {
        this.name_of_person = name_of_person;
    }
}
