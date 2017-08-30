package com.fun.model;

import lombok.Data;

import java.util.Date;

@Data
public class AuthTokenDetails {
    private String username;
    private String roleName;
    private Date expirationDate;
}
