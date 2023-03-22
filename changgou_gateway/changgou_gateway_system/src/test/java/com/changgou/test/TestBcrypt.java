package com.changgou.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class TestBcrypt {
    public static void main(String[] args) {
        for (int i =0 ; i< 10 ;i++){
            String gensalt = BCrypt.gensalt();
            System.out.println("salt:"+gensalt);
            String saltPassword = BCrypt.hashpw("123456", gensalt);
            System.out.println("本次生成的密码:"+saltPassword);
            boolean checkpw = BCrypt.checkpw("1234567", saltPassword);
            System.out.println("密码校验结果:"+checkpw);


        }
    }
}
