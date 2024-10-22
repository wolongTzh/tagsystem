package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.dao.entity.EvalOverviewDecorate;
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

    int AddNewScore(EvalDetail evalDetail);

}
