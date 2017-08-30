package com.fun.service.auth;

import com.fun.entity.SysUser;
import com.fun.mapper.SysUserMapper;
import com.fun.secruity.JsonWenTokenUtil;
import com.fun.secruity.JwtAuthResponse;
import com.fun.secruity.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JsonWenTokenUtil jsonWenTokenUtil;

    @Autowired
    private SysUserMapper userMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    // 普通用户
    private static final String ROLE_USER = "ROLE_USER";

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public JwtAuthResponse logIn(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setUsername(username);
        try {
            // 认证检查
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(username);
            final String token = jsonWenTokenUtil.generateToken(userDetails);
            jwtAuthResponse.setToken(token);
            jwtAuthResponse.setStatus(SIGN_IN_SUCCESS);
        } catch (AuthenticationException e) {
            jwtAuthResponse.setStatus(SIGN_IN_FAILED);
        } finally {
            return jwtAuthResponse;
        }
    }

    @Override
    public JwtAuthResponse logOut(String token) {
        return null;
    }

    /**
     * 用户注册
     * @param userToAdd 注册信息
     * @return
     */
    @Override
    public JwtAuthResponse signUp(SysUser registerUser) {
        final String username = registerUser.getUserName();
        SysUser addUser = new SysUser();
        addUser.setUserName(username);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        if (userMapper.selectOne(addUser) != null) {
            jwtAuthResponse.setStatus(EXIST_SAME_USERNAME);
            return jwtAuthResponse;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = registerUser.getPassword();
        Date createDate = new Date();
        addUser.setPassword(encoder.encode(rawPassword));
        addUser.setCreatedDate(createDate);
        addUser.setLastPasswordResetDate(createDate);
        addUser.setEmail(registerUser.getEmail());
        addUser.setUserRoles(ROLE_USER);
        int insert = userMapper.insert(addUser);
        if (insert == 1) {
            jwtAuthResponse.setStatus(SIGN_UP_SUCCESS);
        } else {
            jwtAuthResponse.setStatus(SIGN_UP_FAILED);
        }
        return jwtAuthResponse;
    }

    @Override
    public JwtAuthResponse refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jsonWenTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setUsername(username);
        if (jsonWenTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            jwtAuthResponse.setToken(jsonWenTokenUtil.refreshToken(token));
            jwtAuthResponse.setStatus(REFRESH_SUCCESS);
        } else {
            jwtAuthResponse.setStatus(REFRESH_FAILED);
        }
        return jwtAuthResponse;
    }
}
