package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.model.TestModelParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

    int addNewScore(EvalDetail evalDetail);

    int uploadTestData(UploadTestDataParam param) throws IOException;

    int uploadModel(UploadModelParam param) throws IOException;

    int runTest(TestModelParam param) throws IOException;

}
