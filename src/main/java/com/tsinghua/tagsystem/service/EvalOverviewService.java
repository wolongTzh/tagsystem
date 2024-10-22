package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.EvalOverviewDecorate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EvalOverviewService {

   /**
     * 获取评估总览
     * @param userId
     * @return
     */
    List<EvalOverviewDecorate> getEvalOverview(int userId);

    /** 新增评估总览数据, 传参为任务名称（taskName）还有任务介绍（taskIntro）
     * @Param("taskName") String taskName, @Param("taskIntro") String taskIntro
     * @return
     */
    int addEvalOverview(String taskName, String taskIntro, int userId, String userName);

    /** 更新评估总览数据，传参为任务名称（taskName）还有任务介绍（taskIntro）还有任务id（taskId）
     * @Param("taskName") String taskName, @Param("taskIntro") String taskIntro, @Param("taskId") int taskId
     * @return
     */
    int updateEvalOverview(String taskName, String taskIntro, int taskId);

    /** 删除评估总览数据，传参为任务id（taskId）
     * @Param("taskId") int taskId
     * @return
     */
    int deleteEvalOverview(int taskId);
}
