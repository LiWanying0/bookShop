package com.nit.book.shop.security;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nit.book.shop.entity.User;
import com.nit.book.shop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .eq("studentid", username);

        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(user.getStudentid())
            .password(user.getPassword())
            .authorities(Collections.emptyList()).build();
//            .accountLocked(user.getLocked()).build();
        return userDetails;
    }

    /**
     * 将用户角色转换成Security需要的
     *
     * @param roles
     * @return
     */
//    private Collection<GrantedAuthority> getAuthorities(List<SsoRole> roles) {
//        if (!CollectionUtils.isEmpty(roles)) {
//            String[] roleNames = roles.stream().map(ssoRole -> ssoRole.getId().toString()).collect(Collectors.toList()).toArray(new String[roles.size()]);
//            return AuthorityUtils.createAuthorityList(roleNames);
//        } else {
//            return AuthorityUtils.createAuthorityList();
//        }
//    }

}
