package com.tsinghua.tagsystem.model.params;

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
    int evalOverviewId;
    MultipartFile code;
    MultipartFile env;
    String cmd;
}
