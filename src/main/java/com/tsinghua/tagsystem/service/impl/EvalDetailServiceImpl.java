package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.DataInfoMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalDetailMapper;
import com.tsinghua.tagsystem.dao.mapper.EvalOverviewMapper;
import com.tsinghua.tagsystem.dao.mapper.ModelInfoMapper;
import com.tsinghua.tagsystem.model.TestModelParam;
import com.tsinghua.tagsystem.service.EvalDetailService;
import com.tsinghua.tagsystem.service.EvalOverviewService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvalDetailServiceImpl implements EvalDetailService {

    @Autowired
    EvalOverviewMapper evalOverviewMapper;

    @Autowired
    EvalDetailMapper evalDetailMapper;

    @Autowired
    ModelInfoMapper modelInfoMapper;

    @Autowired
    DataInfoMapper dataInfoMapper;


    @Override
    public EvalDetailDecorate getEvalDetail(int evalOverviewId) {
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        // 使用该evalOverview构建一个EvalDetailDecorate对象
        EvalDetailDecorate evalDetailDecorate = new EvalDetailDecorate(evalOverview);
        evalDetailDecorate.setEvalDetailList(evalDetailMapper.selectList(new QueryWrapper<EvalDetail>().eq("eval_overview_id", evalOverviewId)));
        List<ModelInfo> modelInfoList = modelInfoMapper.selectList(new QueryWrapper<ModelInfo>());
        List<ExistModel> existModelList = new ArrayList<>();
        // 使用modelInfoList来构建existModelList
        existModelList = modelInfoList.stream().map(ExistModel::new).collect(Collectors.toList());
        List<ExistTestData> testDataList = new ArrayList<>();
        // 对testDataList做相同操作
        List<DataInfo> dataInfoList = dataInfoMapper.selectList(new QueryWrapper<DataInfo>().eq("data_type","EVAL"));
        testDataList = dataInfoList.stream().map(ExistTestData::new).collect(Collectors.toList());
        evalDetailDecorate.setTestDataList(testDataList);
        evalDetailDecorate.setModelList(existModelList);
        return evalDetailDecorate;
    }

    @Override
    public int updateEvalDetail(EvalDetailDecorate evalDetailDecorate) {
        EvalOverview oldEvalOverview = evalOverviewMapper.selectById(evalDetailDecorate.getEvalOverviewId());
        String oldTestIds = oldEvalOverview.getEvalTestIds();
        String newTestIds = evalDetailDecorate.getEvalTestIds();
        String oldAlgoIds = oldEvalOverview.getEvalAlgoIds();
        String newAlgoIds = evalDetailDecorate.getEvalAlgoIds();
        List<String> oldTestIdList = new ArrayList<>();
        if(!StringUtils.isEmpty(oldTestIds)) {
            oldTestIdList = Arrays.asList(oldTestIds.split(","));
        }
        List<String> newTestIdList = Arrays.asList(newTestIds.split(","));
        //在newTestIdList中寻找oldTestIdList的每一个元素，如果不存在，则输出
        List<String> diffTestIdList = oldTestIdList.stream().filter(item -> !newTestIdList.contains(item)).collect(Collectors.toList());
        // 对于algoId做相同的操作
        List<String> oldAlgoIdList = new ArrayList<>();
        if(!StringUtils.isEmpty(oldAlgoIds)) {
            oldAlgoIdList = Arrays.asList(oldAlgoIds.split(","));
        }
        List<String> newAlgoIdList = Arrays.asList(newAlgoIds.split(","));
        List<String> diffAlgoIdList = oldAlgoIdList.stream().filter(item -> !newAlgoIdList.contains(item)).collect(Collectors.toList());
        // 通过使用evalDetailMapper删除所有其testId在diffTestIdList中的记录，还有一个条件是evalOverviewId
        for (String diffTestId : diffTestIdList) {
            evalDetailMapper.delete(new QueryWrapper<EvalDetail>().eq("eval_overview_id", evalDetailDecorate.getEvalOverviewId()).eq("eval_data_id", diffTestId));
        }
        // 对于algoId做相同的操作
        for (String diffAlgoId : diffAlgoIdList) {
            evalDetailMapper.delete(new QueryWrapper<EvalDetail>().eq("eval_overview_id", evalDetailDecorate.getEvalOverviewId()).eq("eval_data_id", diffAlgoId));
        }
        oldEvalOverview.setEvalAlgoIds(newAlgoIds);
        oldEvalOverview.setEvalTestIds(newTestIds);
        oldEvalOverview.setEvalAlgoNames(evalDetailDecorate.getEvalAlgoNames());
        oldEvalOverview.setEvalTestNames(evalDetailDecorate.getEvalTestNames());
        evalOverviewMapper.updateById(oldEvalOverview);
        return 1;
    }

    @Override
    public int startTest(EvalDetail evalDetail) throws IOException {
        String url = "http://192.168.3.39:8081/algo_start";
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(evalDetail));
        return 1;
    }

    @Override
    public int delTestResult(int evalDetailId) {
        return evalDetailMapper.deleteById(evalDetailId);
    }

    @Override
    public int addNewScore(EvalDetail evalDetail) {
        // 设置当前时间置evalDetail的evalTime字段
        evalDetail.setEvalTime(LocalDateTime.now());
        evalDetailMapper.insert(evalDetail);
        return evalDetail.getEvalDetailId();
    }

    @Override
    public int uploadTestData(UploadTestDataParam param) throws IOException {
        String dataName = param.getDataName();
        File destFile = new File("/home/tz/copy-code/docker-pytorch/input/" + param.getEvalUserName() + "-" + dataName + ".json");
        MultipartFile multipartFile = param.getFile();
        multipartFile.transferTo(destFile);
        DataInfo dataInfo = new DataInfo();
        dataInfo.setDataName(dataName);
        dataInfo.setDataCreatorId(param.getEvalUserId());
        dataInfo.setDataCreatorName(param.getEvalUserName());
        dataInfo.setDataPath("/home/tz/copy-code/docker-pytorch/input/" + param.getEvalUserName() + "-" + dataName + ".json");
        dataInfo.setDataGenTime(LocalDateTime.now());
        dataInfo.setDataType("EVAL");
        dataInfo.setDataRelaInfo("");
        dataInfoMapper.insert(dataInfo);
        return dataInfo.getDataId();
    }

    @Override
    public int uploadModel(UploadModelParam param) throws IOException {
        String modelName = param.getModelName();
        File destModelFile = new File("/home/tz/copy-code/docker-pytorch/" + param.getEvalUserName() + "-" + modelName + ".tar.gz");
        File destEnvFile = new File("/home/tz/copy-code/docker-pytorch/" + param.getEvalUserName() + "-" + modelName + "-env.tar.gz");
        MultipartFile modelFile = param.getCode();
        MultipartFile envFile = param.getEnv();
        modelFile.transferTo(destModelFile);
        envFile.transferTo(destEnvFile);
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelCreator(param.getEvalUserName());
        modelInfo.setModelCreatorId(param.getEvalUserId());
        modelInfo.setModelGenTime(LocalDateTime.now());
        modelInfo.setModelName(modelName);
        modelInfo.setModelPath("/home/tz/copy-code/docker-pytorch/" + param.getEvalUserName() + "-" + modelName + ".tar.gz");
        modelInfo.setEnvPath("/home/tz/copy-code/docker-pytorch/" + param.getEvalUserName() + "-" + modelName + "-env.tar.gz");
        modelInfo.setCodeName(param.getCodeName());
        modelInfo.setCmd(param.getCmd());
        modelInfo.setModelVersion("V1");
        modelInfoMapper.insert(modelInfo);
        return modelInfo.getModelId();
    }

    @Override
    public int runTest(TestModelParam param) throws IOException {
        String url = "http://192.168.3.39:8081/eval_upload_model";
        ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
        param.setCmd(modelInfo.getCmd());
        param.setCodeName(modelInfo.getCodeName());
        param.setEnvPath(modelInfo.getEnvPath());
        param.setModelPath(modelInfo.getModelPath());
        System.out.println(JSON.toJSONString(param));
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }
}
