package com.cse.ku.communication;

public class ModelUser {

    String name, email, photo, role, id;

    public ModelUser() {

    }

    public ModelUser(String name, String email, String photo, String role, String id) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.role = role;
        this.id = id;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
