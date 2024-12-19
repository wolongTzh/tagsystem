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
    String originCodeName;
    String originEnvName;
    String cmd;
    int workerId;
    String workerName;
    String modelType;
}
