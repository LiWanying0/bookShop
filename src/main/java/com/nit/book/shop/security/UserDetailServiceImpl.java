//package com.nit.book.shop.security;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.video.common.api.entity.TbUser;
//import com.video.mapper.TbUserMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//
//@Component
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private TbUserMapper rbacUserMapper;
//
////    @Autowired
////    private TbRbacUserRoleMapper rbacUserRoleMapper;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//            .eq("username", username);
//
//        TbUser user = rbacUserMapper.selectOne(queryWrapper);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("用户不存在");
//        }
//
////        QueryWrapper<TbRbacUserRole> userRoleQueryWrapper = new QueryWrapper<>();
////        userRoleQueryWrapper.eq("user_id", user.getId());
////
////        List<TbRbacUserRole> userRoles = rbacUserRoleMapper.selectList(userRoleQueryWrapper);
//
//        UserDetails userDetails = User
//            .withUsername(user.getUsername())
//            .password(user.getPassword())
//            .authorities(Collections.emptyList())
//            .accountLocked(user.getLocked()).build();
//        return userDetails;
//    }
//
//    /**
//     * 将用户角色转换成Security需要的
//     *
//     * @param roles
//     * @return
//     */
////    private Collection<GrantedAuthority> getAuthorities(List<SsoRole> roles) {
////        if (!CollectionUtils.isEmpty(roles)) {
////            String[] roleNames = roles.stream().map(ssoRole -> ssoRole.getId().toString()).collect(Collectors.toList()).toArray(new String[roles.size()]);
////            return AuthorityUtils.createAuthorityList(roleNames);
////        } else {
////            return AuthorityUtils.createAuthorityList();
////        }
////    }
//
//}
