package com.tsinghua.tagsystem.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UploadTrainParam {

    int evalOverviewId;
    int evalUserId;
    String evalUserName;
    String modelName;
    String modelTrainDataName;
    String suffix;
}
