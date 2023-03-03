package com.example.demo.test;

import com.google.gson.Gson;

import java.util.List;

public class Testimonial {
    private String id;
    private String step;
    private String lang;
    private String descriptiveName;
    private List<String> video;
    private int number;
    private String name;
    private List<String> videoUrl;
    private String key;

    public String getId() {
        return id;
    }

    public String getStep() {
        return step;
    }

    public String getLang() {
        return lang;
    }

    public String getDescriptiveName() {
        return descriptiveName;
    }

    public List<String> getVideo() {
        return video;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public List<String> getVideoUrl() {
        return videoUrl;
    }

    public String getKey() {
        return key;
    }

    public static Testimonial fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Testimonial.class);
    }
}