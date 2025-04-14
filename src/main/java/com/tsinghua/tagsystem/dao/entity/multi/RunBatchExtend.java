package com.tsinghua.tagsystem.dao.entity.multi;

import com.tsinghua.tagsystem.dao.entity.LlmTask;
import com.tsinghua.tagsystem.dao.entity.ModelInfo;
import com.tsinghua.tagsystem.dao.entity.RunBatchInfo;
import com.tsinghua.tagsystem.dao.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RunBatchExtend extends RunBatchInfo {

    LlmTask llmTask;
    ModelInfo modelInfo;

    public RunBatchExtend(RunBatchInfo runBatchInfo){
        this.setRbiId(runBatchInfo.getRbiId());
        this.setStatus(runBatchInfo.getStatus());
        this.setDocPath(runBatchInfo.getDocPath());
        this.setModelType(runBatchInfo.getModelType());
        this.setDbName(runBatchInfo.getDbName());
        this.setScriptPath(runBatchInfo.getScriptPath());
        this.setModelId(runBatchInfo.getModelId());
        this.setOriginOutputPath(runBatchInfo.getOriginOutputPath());
        this.setJsonOutputPath(runBatchInfo.getJsonOutputPath());
        this.setOverviewId(runBatchInfo.getOverviewId());
        this.setTaskName(runBatchInfo.getTaskName());
    }
}
