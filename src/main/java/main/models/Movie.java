package main.models;

import java.io.Serializable;

/**
 * Created by ahmed on 13/08/16.
 */
public class Movie implements Serializable {
    private static final long serialVersionUID = -6099312954099962806L;
    private String poster;
    private String description;
    private String title;
    private String release_date;
    private String language;
    private Float rate;
    private String popular;
    private String id;
    @Override
    public String toString() {
        return poster+" - "+description+" - "+title+" - "+release_date+" - "+language+" - "+rate+" - "+popular+" - "+id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }
}
