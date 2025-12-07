package org.kush.vaultyauth.controller;

import lombok.Getter;

@Getter
public class OAuthException extends Exception {
    public OAuthException(OAuth2Errors errorType) {
        super(errorType.toString());
    }
}

