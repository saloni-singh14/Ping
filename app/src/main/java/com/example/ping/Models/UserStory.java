package com.example.ping.Models;

import java.util.ArrayList;

public class UserStory {
    private String name, profileImage;
    private long lastUpdated;
    private ArrayList<Story> statuses;

    public UserStory() {
    }

    public UserStory(String name, String profileImage, long lastUpdated, ArrayList<Story> statuses) {
        this.name = name;
        this.profileImage = profileImage;
        this.lastUpdated = lastUpdated;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<Story> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Story> statuses) {
        this.statuses = statuses;
    }
}
