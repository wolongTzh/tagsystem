package com.tsinghua.tagsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.dao.entity.AlgoInfo;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.dao.entity.EvalOverviewDecorate;
import com.tsinghua.tagsystem.dao.mapper.AlgoInfoMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.model.params.BuildCompareTaskParam;
import com.tsinghua.tagsystem.model.params.BuildPromoteTaskParam;
import com.tsinghua.tagsystem.service.EvalOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvalOverviewServiceImpl implements EvalOverviewService {

    @Autowired
    EvalOverviewMapper evalOverviewMapper;
    @Autowired
    AlgoInfoMapper algoInfoMapper;


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
    public int addEvalOverview(EvalOverview evalOverviewParam) {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName(evalOverviewParam.getEvalOverviewName())
                .evalOverviewIntro(evalOverviewParam.getEvalOverviewIntro())
                .evalOverviewUserId(evalOverviewParam.getEvalOverviewUserId())
                .evalOverviewUserName(evalOverviewParam.getEvalOverviewUserName())
                .evalOverviewType(evalOverviewParam.getEvalOverviewType())
                // 使用当前时间赋值OverviewTime
                .evalOverviewTime(LocalDateTime.now())
                .build();
        evalOverviewMapper.insert(evalOverview);
        return evalOverview.getEvalOverviewId();
    }

    @Override
    public int updateEvalOverview(EvalOverview evalOverview) {
        QueryWrapper<EvalOverview> param = new QueryWrapper<>();
        // 判断需要taskId相同的条件
        param.eq("eval_overview_id", evalOverview.getEvalOverviewId());
        return evalOverviewMapper.update(EvalOverview.builder()
                .evalOverviewName(evalOverview.getEvalOverviewName())
                .evalOverviewIntro(evalOverview.getEvalOverviewIntro())
                .build(), param);
    }

    @Override
    public int deleteEvalOverview(int taskId) {
        return evalOverviewMapper.deleteById(taskId);
    }

    @Override
    public int buildPromoteTask(BuildPromoteTaskParam param) throws IOException {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName(param.getEvalOverviewName())
                .evalOverviewIntro(param.getEvalOverviewIntro())
                .evalOverviewUserId(param.getEvalUserId())
                .evalOverviewUserName(param.getEvalUserName())
                .evalOverviewType("优化任务")
                .build();
        int evalOverviewId = addEvalOverview(evalOverview);
        String algoName = param.getEvalOverviewName();
        File destModelFile = new File("/home/tz/copy-code/docker-pytorch-model/" + param.getEvalUserName() + "-" + algoName + ".tar.gz");
        File destEnvFile = new File("/home/tz/copy-code/docker-pytorch-model/" + param.getEvalUserName() + "-" + algoName + "-env.tar.gz");
        MultipartFile modelFile = param.getCode();
        MultipartFile envFile = param.getEnv();
        modelFile.transferTo(destModelFile);
        envFile.transferTo(destEnvFile);
        AlgoInfo algoInfo = new AlgoInfo();
        algoInfo.setAlgoCreatorName(param.getEvalUserName());
        algoInfo.setAlgoCreatorId(param.getEvalUserId());
        algoInfo.setAlgoGenTime(LocalDateTime.now());
        algoInfo.setAlgoName(algoName);
        algoInfo.setAlgoPath("/home/tz/copy-code/docker-pytorch-model/" + param.getEvalUserName() + "-" + algoName + ".tar.gz");
        algoInfo.setEnvPath("/home/tz/copy-code/docker-pytorch-model/" + param.getEvalUserName() + "-" + algoName + "-env.tar.gz");
        algoInfo.setCmd(param.getCmd());
        algoInfo.setAlgoVersion("V1");
        algoInfo.setEvalOverviewId(evalOverviewId);
        algoInfoMapper.insert(algoInfo);
        return evalOverviewId;
    }

    @Override
    public int buildCompareTask(BuildCompareTaskParam param) throws IOException {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName(param.getEvalOverviewName())
                .evalOverviewIntro(param.getEvalOverviewIntro())
                .evalOverviewUserId(param.getEvalUserId())
                .evalOverviewUserName(param.getEvalUserName())
                .evalOverviewType("对比任务-" + param.getSource())
                .build();
        int evalOverviewId = addEvalOverview(evalOverview);
        return evalOverviewId;
    }
}
