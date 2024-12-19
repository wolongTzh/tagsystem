package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.model.PathCollection;
import com.tsinghua.tagsystem.model.params.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface EvalDetailService {

   /**
     * 获取具体某一个任务的表格
     * @param evalOverviewId
     * @return
     */
    EvalDetailDecorate getEvalDetail(int evalOverviewId);

    /**
     * 更新某一个具体任务的表格
     */
    int updateEvalDetail(EvalDetailDecorate evalDetailDecorate);

    int startTest(EvalDetail evalDetail) throws IOException;

    int delTestResult(int evalDetailId);

    int delModel(int modelId);

    int addNewScore(EvalDetail evalDetail);

    int updateScore(EvalDetail evalDetail);

    int updateTrainMsg(ModelInfo modelInfo);

    int finishTrain(int evalOverviewId, int modelId);

    int uploadTestData(UploadTestDataParam param) throws IOException;

    int uploadModel(UploadModelParam param) throws IOException;

    int uploadCheckpoint(UploadCheckpointParam param) throws IOException;

    int uploadHugModel(UploadHugModelParam param) throws IOException;

    int uploadTrain(UploadTrainParam param);

    int runTest(RunTestModelParam param) throws IOException;

    int runTestPromote(RunTestModelParam param) throws IOException;

    int stopTask(StopTaskParam param) throws IOException;

    int runTestHug(RunTestModelParam param) throws IOException;

    int runTrain(RunTestModelParam param) throws IOException;

    String comparePredictAnswer(int modelId, int testDataId) throws IOException;

    int deleteModel(int modelId, int evalOverviewId);

    int deleteTrainingModel(int modelId, int evalOverviewId);

    int deleteTestData(int testDataId, int evalOverviewId);

    String getStatus(int evalDetailId);

}
