package com.pranks.doctalk.Education.UploaderClass;

import android.net.Uri;

public class UploadNotes {
    String noteTitle,noteDis,key,uploadername;
    long timestamp;
    String filepath;

    public String getUploadername() {
        return uploadername;
    }

    public void setUploadername(String uploadername) {
        this.uploadername = uploadername;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UploadNotes(String filepath, String noteTitle, String noteDis, long timestamp, String uploadername) {
        this.filepath = filepath;
        this.noteTitle = noteTitle;
        this.noteDis = noteDis;
        this.timestamp=timestamp;
        this.uploadername=uploadername;
    }

    public UploadNotes(String filepath, String noteTitle, String noteDis, String key,long timestamp,String uploadername) {
        this.filepath = filepath;
        this.noteTitle = noteTitle;
        this.noteDis = noteDis;
        this.key = key;
        this.timestamp=timestamp;
        this.uploadername=uploadername;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDis() {
        return noteDis;
    }

    public void setNoteDis(String noteDis) {
        this.noteDis = noteDis;
    }
}
