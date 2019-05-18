package com.example.michel.mycalendar2.authentication;

public interface ServerAuthenticate {
    public String userSignUp(final String name, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String email, final String pass) throws Exception;
}
