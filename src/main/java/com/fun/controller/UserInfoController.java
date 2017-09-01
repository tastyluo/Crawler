package com.fun.controller;

import com.fun.model.UserInfo;
import com.fun.secruity.JsonWenTokenUtil;
import com.fun.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 所属公司： 华信联创技术工程有限公司
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-09-01 10:01
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JsonWenTokenUtil jsonWenTokenUtil;

    @Autowired
    UserInfoService userInfoService;

    /**
     *
     * @param request
     * @return UserInfo
     */
    @PostAuthorize("hasRole('ROLE_ADMIN') || returnObject.getUsername() == authentication.principal.username")
    @RequestMapping("/get")
    public UserInfo getUserInfo(HttpServletRequest request) {
        String token = jsonWenTokenUtil.getTokenFromRequestHeader(request.getHeader(tokenHeader));
        String username = jsonWenTokenUtil.getUsernameFromToken(token);
        return userInfoService.getUserInfoByUsername(username);
    }

}
