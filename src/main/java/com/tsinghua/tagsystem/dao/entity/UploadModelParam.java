package com.tsinghua.tagsystem.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UploadModelParam {

    int evalUserId;
    String evalUserName;
    String modelName;
    MultipartFile code;
    MultipartFile env;
    String codeName;
    String cmd;
}
