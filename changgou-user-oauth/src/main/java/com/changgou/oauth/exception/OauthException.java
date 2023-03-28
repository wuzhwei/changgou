package com.changgou.oauth.exception;

import com.changgou.common.exception.ExceptionMessage;
import lombok.Getter;

/**
 * @Description: 权限认证异常
 */
@Getter
public class OauthException extends RuntimeException {
    private static final long serialVersionUID = 8482500389681789675L;

    private ExceptionMessage exceptionMessage;

    public OauthException(ExceptionMessage exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

}