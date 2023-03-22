package system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.changgou.system.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import system.constant.AdminStatusEnum;
import system.dao.AdminMapper;
import system.exception.AdminException;
import system.service.AdminService;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author: wzw
 * @Description: 管理员服务实现
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    /**
     * 查看所有用户
     * @return
     */
    @Override
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    @Override
    public List<Admin> findList(@NotNull Map<String,Object> searchMap) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap != null){
            //用户名
            String loginName = Convert.toStr(searchMap.get("loginName"));
            if(StrUtil.isNotEmpty(loginName)){
                criteria.andLike("loginName","%"+loginName+"%");

            }
            //密码
            String password = Convert.toStr(searchMap.get("password"));
            if (StrUtil.isNotEmpty(password)){
                criteria.andLike("password","%"+password+"%");
            }
            //状态
            String status = Convert.toStr(searchMap.get("status"));
            if(StrUtil.isNotEmpty(status)){
                criteria.andLike("status",status);
            }
            //id
            String id = Convert.toStr(searchMap.get("id"));
            if(StrUtil.isNotEmpty(id)){
                criteria.andLike("id",id);
            }
        }
        return adminMapper.selectByExample(example);
    }


    /**
     * 根据id查找
     * @param id 管理员id
     * @return
     */

    @Override
    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新
     * @param admin 管理员数据
     */

    @Override
    public void updateAdmin(Admin admin) {
        String newPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        admin.setPassword(newPassword);
        adminMapper.selectByPrimaryKey(admin);

    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void deleteByAdminId(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }


    /**
     * 新增
     * @param admin 管理员数据
     */
    @Override
    public void addAdmin(Admin admin) {
        Admin adminOne = adminMapper.selectOne(admin);
        if (adminOne !=null){
            throw new AdminException(AdminStatusEnum.ADMIN_EXIST);
        }
        String password = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        admin.setPassword(password);
        adminMapper.insert(admin);
    }

    /**
     * 登录
     * @param admin
     * @return
     */
    @Override
    public boolean  login(Admin admin) {
        Admin admin1 = new Admin();
        admin1.setLoginName(admin.getLoginName());
        admin1.setStatus("1");
        Admin admin2 = adminMapper.selectOne(admin1);//数据库查询出的对象
        if(admin2 == null){
            return false;
        }else{
            //验证密码，Bcrypt为spring的包，第一个参数为明文密码，第二个参数为密文密码
            return BCrypt.checkpw(admin.getPassword(),admin2.getPassword());
        }


    }

}