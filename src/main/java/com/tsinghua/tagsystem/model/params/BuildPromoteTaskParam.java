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
public class BuildPromoteTaskParam {

    String evalOverviewName;
    String evalOverviewIntro;
    int evalUserId;
    String evalUserName;
    MultipartFile code;
    MultipartFile env;
    String cmd;
    int workerId;
    String workerName;
}
