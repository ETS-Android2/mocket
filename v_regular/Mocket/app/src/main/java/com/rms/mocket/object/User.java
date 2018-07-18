package com.rms.mocket.object;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getlastName() {
        return this.lastName;
    }


}
