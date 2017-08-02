package com.fun.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserInfo {
    private String id;
    private String username;
    private String password;
    private String email;
    private Date lastPasswordResetDate;
    private List<String> roles;
}
