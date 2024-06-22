/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.utils;

import com.widyatama.libraryapp.models.Admin;

public class AuthUtils {
    private static AuthUtils instance;
    private Admin loggedInAdmin;

    private AuthUtils() {
        // Private constructor to prevent instantiation
    }

    public static synchronized AuthUtils getInstance() {
        if (instance == null) {
            instance = new AuthUtils();
        }
        return instance;
    }

    public Admin getLoggedInAdmin() {
        return loggedInAdmin;
    }

    public void setLoggedInAdmin(Admin loggedInAdmin) {
        this.loggedInAdmin = loggedInAdmin;
    }

    public void clearLoggedInAdmin() {
        this.loggedInAdmin = null;
    }
}
