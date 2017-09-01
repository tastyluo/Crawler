package com.fun.service.auth;

import com.fun.entity.SysUser;
import com.fun.mapper.SysUserMapper;
import com.fun.secruity.JwtUser;
import com.fun.secruity.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser queryUser = new SysUser();
        queryUser.setUsername(username);
        SysUser user = userMapper.selectOne(queryUser);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
