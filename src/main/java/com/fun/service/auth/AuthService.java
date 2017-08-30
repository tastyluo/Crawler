package com.fun.service.auth;

import com.fun.entity.SysUser;
import com.fun.secruity.JwtAuthResponse;

public interface AuthService {
    // 登录失败
    Integer SIGN_IN_FAILED = 1101;
    // 登陆成功
    Integer SIGN_IN_SUCCESS = 1100;

    // 注册失败
    Integer SIGN_UP_FAILED = 1200;
    // 注册成功
    Integer SIGN_UP_SUCCESS = 1201;
    // 存在相同的用户名
    Integer EXIST_SAME_USERNAME = 1203;

    // 刷新token失败
    Integer REFRESH_FAILED = 1300;
    // 刷新token成功
    Integer REFRESH_SUCCESS = 1301;

    JwtAuthResponse signUp(SysUser userToRegister);
    JwtAuthResponse logIn(String username, String password);
    JwtAuthResponse logOut(String token);
    JwtAuthResponse refresh(String oldToken);
}
