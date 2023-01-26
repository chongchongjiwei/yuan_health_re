package com.yuan.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yuan.UserService;
import com.yuan.dao.PermissionDao;
import com.yuan.dao.RoleDao;
import com.yuan.dao.UserDao;
import com.yuan.pojo.Permission;
import com.yuan.pojo.Role;
import com.yuan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    /**
     * &#064;TODO //需要改进
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {

        User user = userDao.findByUsername(username);
        if(user==null)
            return null;
        Integer id = user.getId();
        Set<Role> roles = roleDao.findByUserId(id);
        if(roles!=null &&roles.size()>0)
        {
            //这个遍历是引用
            for (Role role : roles) {
                Integer id1 = role.getId();
                Set<Permission> permissions = permissionDao.findByRoleId(id1);
                if(permissions != null && permissions.size() > 0){
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
