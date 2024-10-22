package com.tsinghua.tagsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.EvalOverviewDecorate;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.service.EvalOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvalOverviewServiceImpl implements EvalOverviewService {

    @Autowired
    EvalOverviewMapper evalOverviewMapper;


    @Override
    public List<EvalOverviewDecorate> getEvalOverview(int userId) {
        QueryWrapper<EvalOverview> param = new QueryWrapper<>();
        param.eq("eval_overview_user_id", userId);
        // 使用evalOverviewMapper中的方法来帮助我返回需要的数据，参数为userId
        List<EvalOverview> evalOverviewList = evalOverviewMapper.selectList(param);
        List<EvalOverviewDecorate> evalOverviewDecorateList = new ArrayList<>();
        // 将evalOverviewList中的每一条数据来构造evalOverviewDecorateList的数据
        evalOverviewDecorateList = evalOverviewList.stream().map(EvalOverviewDecorate::new).collect(Collectors.toList());
        // 遍历evalOverviewList
        for (EvalOverviewDecorate evalOverviewDecorate : evalOverviewDecorateList) {
            // 判断evalOverview中的evalTestIds字段，根据该字段中,符号的数量来赋值evalOverview中的evalTestNum字段
            if (evalOverviewDecorate.getEvalTestIds() != null) {
                evalOverviewDecorate.setEvalTestNum(evalOverviewDecorate.getEvalTestIds().split(",").length);
            }
            // 判断evalOverview中的evalAlgoIds字段，根据该字段中,符号的数量来赋值evalOverview中的evalAlgoNum字段
            if (evalOverviewDecorate.getEvalAlgoIds() != null) {
                evalOverviewDecorate.setEvalAlgoNum(evalOverviewDecorate.getEvalAlgoIds().split(",").length);
            }
        }
        return evalOverviewDecorateList;
    }

    @Override
    /** 通过使用任务名称，任务简介，用户id等字段来新增数据，使用evalOverviewService
     * @param taskName 任务名称
     * @param taskIntro 任务简介
     * @param userId 用户id
     */
    public int addEvalOverview(String taskName, String taskIntro, int userId, String userName) {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName(taskName)
                .evalOverviewIntro(taskIntro)
                .evalOverviewUserId(userId)
                .evalOverviewUserName(userName)
                // 使用当前时间赋值OverviewTime
                .evalOverviewTime(LocalDateTime.now())
                .build();
        evalOverviewMapper.insert(evalOverview);
        return evalOverview.getEvalOverviewId();
    }

    @Override
    public int updateEvalOverview(String taskName, String taskIntro, int taskId) {
        QueryWrapper<EvalOverview> param = new QueryWrapper<>();
        // 判断需要taskId相同的条件
        param.eq("eval_overview_id", taskId);
        return evalOverviewMapper.update(EvalOverview.builder()
                .evalOverviewName(taskName)
                .evalOverviewIntro(taskIntro)
                .build(), param);
    }

    @Override
    public int deleteEvalOverview(int taskId) {
        return evalOverviewMapper.deleteById(taskId);
    }
}
