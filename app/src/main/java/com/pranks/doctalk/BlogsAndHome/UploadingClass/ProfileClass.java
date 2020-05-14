package com.pranks.doctalk.BlogsAndHome.UploadingClass;

public class ProfileClass {
    String name,email,profileimg;

    public ProfileClass(String name, String email, String profileimg) {
        this.name = name;
        this.email = email;
        this.profileimg = profileimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }
}
