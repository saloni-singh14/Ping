package com.example.ping.Models;

public class Story {
    private String imageUrl;
    private long timeStamp;

    public Story() {
    }

    public Story(String imageUrl, long timeStamp) {
        this.imageUrl = imageUrl;
        this.timeStamp = timeStamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
