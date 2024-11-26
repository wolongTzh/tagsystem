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
public class buildPromoteTaskParam {

    String evalOverviewName;
    String evalOverviewIntro;
    int evalUserId;
    String evalUserName;
    String algoName;
    MultipartFile code;
    MultipartFile env;
    String cmd;
    int evalOverviewId;
}
