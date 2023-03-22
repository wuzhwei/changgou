package com.changgou.system.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_admin")
public class Admin implements Serializable {
    private static final long serialVersionUID = -6812066923497321963L;
    /**
     * 管理员id
     */
    @Id
    private Integer id;
    /**
     * 用户名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态
     */
    private String status;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
