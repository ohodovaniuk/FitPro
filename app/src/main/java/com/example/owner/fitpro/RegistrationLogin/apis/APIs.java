package com.example.owner.fitpro.RegistrationLogin.apis;

/**
 * Created by Shirwa on 2018-02-17.
 */

public class APIs {
    private UsersAPI usersAPI = new UsersAPI();

    public APIs() {

    }

    public UsersAPI getUsersAPI() {
        return this.usersAPI;
    }
}
