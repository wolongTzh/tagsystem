package com.tsinghua.tagsystem.exception;

import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import lombok.Data;

/**
 * 业务异常类
 *
 * @author tanzheng
 * @date 2022/10/12
 */
@Data
public class BusinessException extends RuntimeException {

    private Integer code;

    private String message;

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(BusinessExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
    }
}
