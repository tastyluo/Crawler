package com.fun.service;

import com.fun.mapper.UserInfoMapper;
import com.fun.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 所属公司： 华信联创技术工程有限公司
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-09-01 10:54
 */
@Service
public class UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserInfo getUserInfoByUsername(String username) {
        UserInfo query = new UserInfo();
        query.setUsername(username);
        return userInfoMapper.selectOne(query);
    }
}
