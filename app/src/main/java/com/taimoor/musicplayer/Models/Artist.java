package com.taimoor.musicplayer.Models;

import java.io.Serializable;

public class Artist implements Serializable {

    public String id;
    public String name;
    public String role;
    public String image;
    public String type;
    public String perma_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPerma_url() {
        return perma_url;
    }

    public void setPerma_url(String perma_url) {
        this.perma_url = perma_url;
    }
}
