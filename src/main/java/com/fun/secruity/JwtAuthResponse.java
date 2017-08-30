package com.fun.secruity;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtAuthResponse implements Serializable {

    private String token;

    private Integer status;

    private String username;

    public JwtAuthResponse() {
        this.token = "";
    }
}
