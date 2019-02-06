package org.mitre.fidouaf.model;

import com.google.gson.annotations.Expose;

public class FidoUafNotification {

    @Expose
    private String fidoOtp;

    @Expose
    private String authId;

    @Expose
    private String username;

    @Expose
    private String aaid;

    @Expose
    private String fidoOperation;

    @Expose
    private String keyId;


    public String getFidoOtp() {
        return fidoOtp;
    }

    public void setFidoOtp(String fidoOtp) {
        this.fidoOtp = fidoOtp;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAaid() {
        return aaid;
    }

    public void setAaid(String aaid) {
        this.aaid = aaid;
    }

    public String getFidoOperation() {
        return fidoOperation;
    }

    public void setFidoOperation(String fidoOperation) {
        this.fidoOperation = fidoOperation;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}