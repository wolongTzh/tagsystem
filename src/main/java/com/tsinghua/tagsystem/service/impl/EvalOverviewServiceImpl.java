package com.tsinghua.tagsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.config.AlchemistPathConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.*;
import com.tsinghua.tagsystem.model.params.BuildCompareTaskParam;
import com.tsinghua.tagsystem.model.params.BuildLLMTaskParam;
import com.tsinghua.tagsystem.model.params.BuildPromoteTaskParam;
import com.tsinghua.tagsystem.service.EvalOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
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
    @Autowired
    EvalDetailMapper evalDetailMapper;

    @Autowired
    ModelInfoMapper modelInfoMapper;

    @Autowired
    OverviewUserRelaMapper overviewUserRelaMapper;

    String modelCodePath;
    String checkpointPath;
    String testDataPath;
    String customizeModeModelPath;

    EvalOverviewServiceImpl(AlchemistPathConfig config) {
        modelCodePath = config.getModelCodePath();
        checkpointPath = config.getCheckpointPath();
        testDataPath = config.getTestDataPath();
        customizeModeModelPath = config.getCustomizeModeModelPath();
    }


    @Override
    public List<EvalOverviewDecorate> getEvalOverview(int userId) {
        QueryWrapper<OverviewUserRela> relaParam = new QueryWrapper<>();
        relaParam.eq("user_id", userId);
        List<OverviewUserRela> overviewUserRelaList = overviewUserRelaMapper.selectList(relaParam);
        if(overviewUserRelaList == null || overviewUserRelaList.size() == 0) {
            return new ArrayList<>();
        }
        // 以overviewUserRelaList中的overviewId作为条件，查询符合条件的所有EvalOverview，一条语句完成
        List<EvalOverview> evalOverviewList = evalOverviewMapper.selectBatchIds(overviewUserRelaList.stream().map(OverviewUserRela::getOverviewId).collect(Collectors.toList()));
//        QueryWrapper<EvalOverview> param = new QueryWrapper<>();
//        param.eq("eval_overview_user_id", userId);
//        // 使用evalOverviewMapper中的方法来帮助我返回需要的数据，参数为userId
//        List<EvalOverview> evalOverviewList = evalOverviewMapper.selectList(param);
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
                .token(evalOverviewParam.getToken())
                .llmUpType(evalOverviewParam.getLlmUpType())
                .build();
        evalOverviewMapper.insert(evalOverview);
        OverviewUserRela overviewUserRela = OverviewUserRela.builder()
                .overviewId(evalOverview.getEvalOverviewId())
                .userId(evalOverview.getEvalOverviewUserId())
                .userName(evalOverview.getEvalOverviewUserName())
                .build();
        overviewUserRelaMapper.insert(overviewUserRela);
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
        // OverviewUserRela中所有overview_id为taskId的数据记录，使用overviewUserRelaMapper，一条语句完成
        evalDetailMapper.delete(new QueryWrapper<EvalDetail>().eq("eval_overview_id", taskId));
        overviewUserRelaMapper.delete(new QueryWrapper<OverviewUserRela>().eq("overview_id", taskId));
        AlgoInfo algoInfo = algoInfoMapper.selectOne(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", taskId));
        if (algoInfo != null) {
            modelInfoMapper.delete(new QueryWrapper<ModelInfo>().eq("algo_id", algoInfo.getAlgoId()));
            algoInfoMapper.delete(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", taskId));
        }
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
        OverviewUserRela overviewUserRela = OverviewUserRela.builder()
                .overviewId(evalOverviewId)
                .userId(param.getWorkerId())
                .userName(param.getWorkerName())
                .build();
        overviewUserRelaMapper.insert(overviewUserRela);
        String algoName = param.getEvalOverviewName();
//        MultipartFile modelFile = param.getCode();
//        MultipartFile envFile = param.getEnv();
//        File destModelFile = new File(modelCodePath + param.getEvalUserName() + "-" + modelFile.getOriginalFilename());
//        File destEnvFile = new File(modelCodePath + param.getEvalUserName() + "-env-" + modelFile.getOriginalFilename());
//        modelFile.transferTo(destModelFile);
//        envFile.transferTo(destEnvFile);
        AlgoInfo algoInfo = new AlgoInfo();
        algoInfo.setAlgoCreatorName(param.getEvalUserName());
        algoInfo.setAlgoCreatorId(param.getEvalUserId());
        algoInfo.setAlgoGenTime(LocalDateTime.now());
        algoInfo.setAlgoName(algoName);
        algoInfo.setAlgoPath(modelCodePath + param.getEvalUserName() + "-" + param.getOriginCodeName());
        algoInfo.setEnvPath(modelCodePath + param.getEvalUserName() + "-env-" + param.getOriginEnvName());
        algoInfo.setCmd(param.getCmd());
        algoInfo.setAlgoVersion("V1");
        algoInfo.setEvalOverviewId(evalOverviewId);
        algoInfo.setModelType(param.getModelType());
        algoInfo.setTrainCmd(param.getTrainCmd());
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

    @Override
    public int buildLLMTask(BuildLLMTaskParam param) throws IOException {
        EvalOverview evalOverview = EvalOverview.builder()
                .evalOverviewName(param.getEvalOverviewName())
                .evalOverviewIntro(param.getEvalOverviewIntro())
                .evalOverviewUserId(param.getEvalUserId())
                .evalOverviewUserName(param.getEvalUserName())
                .llmUpType(param.getLlmUpType())
                .token(param.getToken())
                .evalOverviewType("大模型任务")
                .build();
        int evalOverviewId = addEvalOverview(evalOverview);
        return evalOverviewId;
    }

    @Override
    public String genLog(int evalOverviewId) {
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        LocalDateTime initTime = evalOverview.getEvalOverviewTime();
        String log = "任务" + evalOverview.getEvalOverviewName() + "开始于" + initTime;
        if(StringUtils.isEmpty(evalOverview.getEvalAlgoIds())) {
            return log;
        }
        String[] modelIdList = evalOverview.getEvalAlgoIds().split(",");
        for (String modelId : modelIdList) {
            ModelInfo modelInfo = modelInfoMapper.selectById(Integer.parseInt(modelId));
            LocalDateTime curModelTime = modelInfo.getModelGenTime();
            // 计算curModelTime与initTime的时间间隔，并以经过了多少天多少小时多少分这样的格式输出
            long days = Duration.between(initTime, curModelTime).toDays();
            long hours = Duration.between(initTime, curModelTime).toHours() % 24;
            long minutes = Duration.between(initTime, curModelTime).toMinutes() % 60;
            log += "<br>模型" + modelInfo.getModelName() + "生成于" + curModelTime + "，花费了" + days + "天" + hours + "小时" + minutes + "分构建而成";
        }
        System.out.println(log);
        return log;
    }
}
