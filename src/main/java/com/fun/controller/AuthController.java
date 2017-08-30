package com.fun.controller;

import com.fun.entity.SysUser;
import com.fun.secruity.JwtAuthRequest;
import com.fun.secruity.JwtAuthResponse;
import com.fun.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    /**
     * 登录
     * @param authenticationRequest 用户名和密码
     * @return token
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtAuthRequest authenticationRequest)
            throws AuthenticationException {
        JwtAuthResponse response = authService.logIn(authenticationRequest.getUsername(),
                authenticationRequest.getPassword());
        // Return the token
        return ResponseEntity.ok(response);
    }

    /**
     * 注册
     * @param addedUser 注册信息
     * @return 注册是否成功代码
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody SysUser addedUser)
            throws AuthenticationException {
        return ResponseEntity.ok(authService.signUp(addedUser));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logOut(@RequestBody SysUser addedUser)
            throws AuthenticationException {
        return ResponseEntity.ok(authService.signUp(addedUser));
    }

    /**
     * 刷新token
     * @param request
     * @return token
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refresh(HttpServletRequest request)
            throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        JwtAuthResponse jwtAuthResponse = authService.refresh(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
