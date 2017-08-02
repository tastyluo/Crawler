package com.fun.service.auth;

import com.fun.entity.SysUser;
import com.fun.mapper.SysUserMapper;
import com.fun.model.UserInfo;
import com.fun.secruity.JsonWenTokenUtil;
import com.fun.secruity.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    // 存在相同的用户名
    private static final String EXIST_SAME_USER = "101";
    // 注册失败
    private static final String REGISTER_FAILD = "102";
    // 注册成功
    private static final String REGISTER_SUCCESS = "103";
    // 普通用户
    private static final String ROLE_USER = "ROLE_USER";

    /**
     * 用户注册
     * @param userToAdd 注册信息
     * @return
     */
    @Override
    public String register(UserInfo userInfo) {
        final String username = userInfo.getUsername();
        SysUser addUser = new SysUser();
        addUser.setUserName(userInfo.getUsername());
        if (userMapper.selectOne(addUser) != null) {
            return EXIST_SAME_USER;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userInfo.getPassword();
        Date create = new Date();
        addUser.setPassword(encoder.encode(rawPassword));
        addUser.setCreatedDate(create);
        addUser.setLastPasswordResetDate(create);
        addUser.setEmail(userInfo.getEmail());
        addUser.setUserRoles(ROLE_USER);
        return userMapper.insert(addUser) == 1 ? REGISTER_SUCCESS : REGISTER_FAILD;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jsonWenTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jsonWenTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jsonWenTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jsonWenTokenUtil.refreshToken(token);
        }
        return null;
    }
}
