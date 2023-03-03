package com.example.demo.test;

public class Video {
    private Long id;
    private String name;
    private String videoHash;
    private String url;
    private String language;

    public Video(Long id, String name, String videoHash, String url, String language) {
        this.id = id;
        this.name = name;
        this.videoHash = videoHash;
        this.url = url;
        this.language = language;
    }

    public Video(String name, String videoHash, String url, String language) {
        this.name = name;
        this.videoHash = videoHash;
        this.url = url;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoHash() {
        return videoHash;
    }

    public void setVideoHash(String videoHash) {
        this.videoHash = videoHash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", videoHash='" + videoHash + '\'' +
                ", url='" + url + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
