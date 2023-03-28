package com.changgou.user.service.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.changgou.user.dao.UserMapper;

import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2020/2/23 23:11
 * @Description: 用户服务实现
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public User findById(String id) {
        return userMapper.selectByPrimaryKey( id );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user) {
        User addUser = userMapper.selectOne( User.builder().name( user.getPhone() ).build() );
        //用户已经存在
        if (addUser != null) {
            throw new RuntimeException("当前用户已经存才");
        }
        //正常新增用户
        user.setUsername( user.getPhone() );
        user.setStatus( "1" );
        user.setCreated( new Date() );
        user.setUpdated( new Date() );
        user.setIsMobileCheck( "1" );
        //加密密码
        String newPassword = this.bCryptPassword( user.getPassword() );
        user.setPassword( newPassword );
        userMapper.insertSelective( user );
        //更新redis中的用户名数据
        redisTemplate.boundSetOps( "username" ).add( user.getUsername() );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective( user );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(String username, String password) {
        //加密密码
        String bCryptPassword = this.bCryptPassword( password );
        int i = userMapper.updatePassword( username, bCryptPassword );
        return i > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePhone(String username, String newPhone) {
        //根据手机号查询用户
        User user = userMapper.selectOne( User.builder().phone( newPhone ).build() );
        if (user != null) {
            //手机号被其他用户占用
            return 2;
        }
        //未占用
        int i = userMapper.updatePhone( username, newPhone );
        if (i <= 0) {
            return 0;
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        userMapper.deleteByPrimaryKey( id );
    }

    @Override
    public List<User> findList(@NotNull Map<String, Object> searchMap) {
        return userMapper.selectByExample( getExample( searchMap ) );
    }

    @Override
    public Page<User> findPage(@NotNull Map<String, Object> searchMap, Integer pageNum, Integer pageSize) {
        return PageHelper
                .startPage( pageNum, pageSize )
                .doSelectPage( () -> userMapper.selectByExample( getExample( searchMap ) ) );
    }



    /**
     * 加密密码
     *
     * @param password 代加密的密码
     */
    private String bCryptPassword(String password) {
        //获取盐
        String salt = BCrypt.gensalt();
        //对用户的密码进行加密
        return BCrypt.hashpw( password, salt );
    }

    /**
     * 条件拼接
     *
     * @param searchMap 查询条件
     * @return example 条件对象
     */
    private Example getExample(@NotNull Map<String, Object> searchMap) {
        Example example = new Example( User.class );
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 用户名
            String username = Convert.toStr( searchMap.get( "username" ) );
            if (StrUtil.isNotEmpty( username )) {
                criteria.andEqualTo( "username", username );
            }
            // 密码，加密存储
            String password = Convert.toStr( searchMap.get( "password" ) );
            if (StrUtil.isNotEmpty( password )) {
                criteria.andEqualTo( "password", password );
            }
            // 注册手机号
            String phone = Convert.toStr( searchMap.get( "phone" ) );
            if (StrUtil.isNotEmpty( phone )) {
                criteria.andLike( "phone", "%" + phone + "%" );
            }
            // 注册邮箱
            String email = Convert.toStr( searchMap.get( "email" ) );
            if (StrUtil.isNotEmpty( email )) {
                criteria.andLike( "email", "%" + email + "%" );
            }
            // 会员来源：1:PC，2：H5，3：Android，4：IOS
            String sourceType = Convert.toStr( searchMap.get( "sourceType" ) );
            if (StrUtil.isNotEmpty( sourceType )) {
                criteria.andEqualTo( "sourceType", sourceType );
            }
            // 昵称
            String nickName = Convert.toStr( searchMap.get( "nickName" ) );
            if (StrUtil.isNotEmpty( nickName )) {
                criteria.andLike( "nickName", "%" + nickName + "%" );
            }
            // 真实姓名
            String name = Convert.toStr( searchMap.get( "name" ) );
            if (StrUtil.isNotEmpty( name )) {
                criteria.andLike( "name", "%" + name + "%" );
            }
            // 使用状态（1正常 0非正常）
            String status = Convert.toStr( searchMap.get( "status" ) );
            if (StrUtil.isNotEmpty( status )) {
                criteria.andEqualTo( "status", status );
            }
            // 头像地址
            String headPic = Convert.toStr( searchMap.get( "headPic" ) );
            if (StrUtil.isNotEmpty( headPic )) {
                criteria.andLike( "headPic", "%" + headPic + "%" );
            }
            // QQ号码
            String qq = Convert.toStr( searchMap.get( "qq" ) );
            if (StrUtil.isNotEmpty( qq )) {
                criteria.andLike( "qq", "%" + qq + "%" );
            }
            // 手机是否验证 （0否  1是）
            String isMobileCheck = Convert.toStr( searchMap.get( "isMobileCheck" ) );
            if (StrUtil.isNotEmpty( isMobileCheck )) {
                criteria.andEqualTo( "isMobileCheck", isMobileCheck );
            }
            // 邮箱是否检测（0否  1是）
            String isEmailCheck = Convert.toStr( searchMap.get( "isEmailCheck" ) );
            if (StrUtil.isNotEmpty( isEmailCheck )) {
                criteria.andEqualTo( "isEmailCheck", isEmailCheck );
            }
            // 性别，1男，0女
            String sex = Convert.toStr( searchMap.get( "sex" ) );
            if (StrUtil.isNotEmpty( sex )) {
                criteria.andEqualTo( "sex", sex );
            }
            // 会员等级
            String userLevel = Convert.toStr( searchMap.get( "userLevel" ) );
            if (StrUtil.isNotEmpty( userLevel )) {
                criteria.andEqualTo( "userLevel", userLevel );
            }
            // 积分
            String points = Convert.toStr( searchMap.get( "points" ) );
            if (StrUtil.isNotEmpty( points )) {
                criteria.andEqualTo( "points", points );
            }
            // 经验值
            String experienceValue = Convert.toStr( searchMap.get( "experienceValue" ) );
            if (StrUtil.isNotEmpty( experienceValue )) {
                criteria.andEqualTo( "experienceValue", experienceValue );
            }
        }
        return example;
    }
}