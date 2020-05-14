package com.pranks.doctalk.BlogsAndHome.UploadingClass;

public class Replyformatt {
    String replyText,nameofreplier,replierimg;

    public Replyformatt(String replyText, String nameofreplier, String replierimg) {
        this.replyText = replyText;
        this.nameofreplier = nameofreplier;
        this.replierimg = replierimg;
    }

    public String getNameofreplier() {
        return nameofreplier;
    }

    public void setNameofreplier(String nameofreplier) {
        this.nameofreplier = nameofreplier;
    }

    public String getReplierimg() {
        return replierimg;
    }

    public void setReplierimg(String replierimg) {
        this.replierimg = replierimg;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
}
