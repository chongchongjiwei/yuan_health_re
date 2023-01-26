package com.yuan.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yuan.UserService;
import com.yuan.pojo.Permission;
import com.yuan.pojo.Role;
import com.yuan.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    /**
     * 根据用户名查询数据库获取信息
     * @param username the username identifying the user whose data is required.
     *
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null){
            //用户名不存在
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        //一个人的角色可能有好多
        Set<Role> roles = user.getRoles();
        for(Role role:roles)
        {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取该角色对应权限
            Set<Permission> permissions= role.getPermissions();
            //批量授权
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

//        封装框架的类
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return userDetails;
    }

}
