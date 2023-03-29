package com.changgou.user.controller;

import com.changgou.common.pojo.PageResult;
import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 查询全部用户数据
     *
     * @return 所有用户信息
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Result<List<User>> findAll() {
        return Result.<List<User>>builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "查询成功" )
                .data( userService.findAll() ).build();
    }

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return 包含用户信息的实体类
     */
    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username) {
        return userService.findById( username );
    }

    /**
     * 根据用户名查询用户数据
     * @return
     */

    @GetMapping("/findUser")
    public Result<User> findById(){
        //获取当前登录用户名
        String username = tokenDecode.getUserInfo().get("username");
        return Result.<User>builder().flag(true)
                .code(StatusCode.OK)
                .message("查询成功")
                .data( userService.findById(username) )
                .build();
    }

    /**
     * 新增用户
     *
     * @param user 新用户数据
     * @return 新增结果
     */
    @PostMapping("/add")
    public Result<Object> addUser(@RequestBody User user) {
        userService.addUser( user );
        return Result.builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "注册成功" ).build();
    }

    /**
     * 修改用户数据
     *
     * @param username 用户名
     * @param user     新用户数据
     * @return 修改结果
     */
    @PutMapping("/{username}")
    public Result<Object> updateUser(@PathVariable("username") String username, @RequestBody User user) {
        user.setUsername( username );
        userService.updateUser( user );
        return Result.builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "修改成功" ).build();
    }

    /**
     * 修改用户密码
     * @param password   用户密码
     * @return 提示信息
     */
    @PutMapping("/resetPassword")
    public Result<Object> resetPassword(@RequestBody String password){
        //获取当前登录用户名
        String username = tokenDecode.getUserInfo().get("username");
        boolean flag = userService.updatePassword(username, password);
        if (flag){
            return Result.builder()
                    .flag(true)
                    .code(StatusCode.OK)
                    .message("密码修改成功")
                    .build();

        }
        return Result.builder()
                .flag(false)
                .code(StatusCode.ERROR)
                .message("密码修改失败")
                .build();
    }

    /**
     *
     * @param phone
     * @return
     */


    @PutMapping("/resetPhone")
    public Result<Object> resetPhone(@RequestParam("phone")String phone){
        //获取当前登录用户
        String username = tokenDecode.getUserInfo().get("username");
        int i = userService.updatePhone(username, phone);
        switch (i){
            case 1:
                return Result.builder()
                        .flag(true)
                        .code(StatusCode.OK)
                        .message("手机号修改成功").build();
            case 2:
                return Result.builder()
                        .flag(false)
                        .code(StatusCode.ERROR)
                        .message("该手机已被占用").build();
            default:
                return Result.builder()
                        .flag(false)
                        .code(StatusCode.ERROR)
                        .message("系统异常，请稍后重试").build();
        }
    }


    /**
     * 根据用户名删除用户数据
     *
     * @param username 用户名
     * @return 删除结果
     */
    @DeleteMapping("/{username}")
    public Result<Object> deleteById(@PathVariable("username") String username) {
        userService.deleteById( username );
        return Result.builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "删除成功" ).build();
    }

    /**
     * 多条件搜索User数据
     *
     * @param searchMap 搜索条件
     * @return 用户数据
     */
    @GetMapping("/search")
    public Result<List<User>> findList(@RequestParam Map<String, Object> searchMap) {
        return Result.<List<User>>builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "查询成功" )
                .data( userService.findList( searchMap ) ).build();
    }

    /**
     * 分页搜索实现
     *
     * @param searchMap 搜索条件
     * @param page      当前页码
     * @param size      每页显示条数
     * @return 分页结果
     */
    @GetMapping("/search/{page}/{size}")
    public Result<PageResult<User>> findPage(@RequestParam Map<String, Object> searchMap, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        Page<User> pageList = userService.findPage( searchMap, page, size );
        return Result.<PageResult<User>>builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "查询成功" )
                .data( PageResult.<User>builder()
                        .total( pageList.getTotal() )
                        .rows( pageList.getResult() ).build() ).build();
    }
}