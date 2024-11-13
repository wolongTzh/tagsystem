package com.tsinghua.tagsystem.exception;


import com.tsinghua.tagsystem.enums.ServerExceptionEnum;
import lombok.Data;

/**
 * 系统异常类
 *
 * @author tanzheng
 * @date 2022/10/12
 */
@Data
public class ServerException extends RuntimeException {

    private Integer code;

    private String message;

    public ServerException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServerException(ServerExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
    }
}
