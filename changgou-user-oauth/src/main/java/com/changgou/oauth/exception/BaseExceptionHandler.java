package com.changgou.oauth.exception;

import com.changgou.common.exception.ExceptionMessage;
import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 公共异常处理
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> error(Exception e) {
        if (e instanceof OauthException) {
            //异常类型为自定义异常，抛出异常信息
            OauthException oAuthException = (OauthException) e;
            ExceptionMessage message = oAuthException.getExceptionMessage();
            return Result.builder()
                    .flag( message.isFlag() )
                    .code( message.getCode() )
                    .message( message.getMessage() ).build();
        }
        return Result.builder()
                .flag( false )
                .code( StatusCode.ERROR )
                .message( e.getMessage() ).build();
    }
}