package com.fun.service.auth;

import com.fun.model.UserInfo;

public interface AuthService {
    String register(UserInfo userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
