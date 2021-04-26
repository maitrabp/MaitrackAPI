package com.maitrack.maitrackapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties("jwt")
public class JWTProperties {
    private static String SECRET_KEY;
    private static int TOKEN_VALIDITY;

    public static String getSECRET_KEY() {
        return SECRET_KEY;
    }

    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public static int getTOKEN_VALIDITY() {
        return TOKEN_VALIDITY;
    }

    public void setTOKEN_VALIDITY(Integer TOKEN_VALIDITY) {
        this.TOKEN_VALIDITY = TOKEN_VALIDITY;
    }
}
