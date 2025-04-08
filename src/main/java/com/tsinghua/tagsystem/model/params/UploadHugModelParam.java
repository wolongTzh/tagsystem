package com.tsinghua.tagsystem.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UploadHugModelParam {

    int evalUserId;
    String evalUserName;
    String hugModelName;
    int evalOverviewId;
    String hugModelPath;
}
