package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tsinghua.tagsystem.config.AlchemistPathConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.*;
import com.tsinghua.tagsystem.model.PathCollection;
import com.tsinghua.tagsystem.model.params.*;
import com.tsinghua.tagsystem.service.EvalDetailService;
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
    @Autowired
    AlgoInfoMapper algoInfoMapper;

    String modelCodePath;
    String checkpointPath;
    String testDataPath;
    String customizeModeModelPath;
    String customizeInterface;
    String promoteInterface;
    String trainInterface;
    String hgfInterface;
    String compareInterface;
    String stopTaskInterface;
    String grepTimeElapse;

    EvalDetailServiceImpl(AlchemistPathConfig config) {
        modelCodePath = config.getModelCodePath();
        checkpointPath = config.getCheckpointPath();
        testDataPath = config.getTestDataPath();
        customizeModeModelPath = config.getCustomizeModeModelPath();
        customizeInterface = config.getCustomizeInterface();
        promoteInterface = config.getPromoteInterface();
        trainInterface = config.getTrainInterface();
        hgfInterface = config.getHgfInterface();
        stopTaskInterface = config.getStopTaskInterface();
        grepTimeElapse = config.getGrepTimeElapse();
        compareInterface = config.getCompareInterface();
    }


    @Override
    public EvalDetailDecorate getEvalDetail(int evalOverviewId) {
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        // 使用该evalOverview构建一个EvalDetailDecorate对象
        EvalDetailDecorate evalDetailDecorate = new EvalDetailDecorate(evalOverview);
        List<EvalDetail> evalDetailList = evalDetailMapper.selectList(new QueryWrapper<EvalDetail>().eq("eval_overview_id", evalOverviewId));
        for (EvalDetail evalDetail : evalDetailList) {
            String score = evalDetail.getEvalScore();
            if (score.contains("开始时间")) {
                double startTime = Double.parseDouble(score.replace("开始时间", ""));
                // 计算当前时间到开始时间的时间间隔
                double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
                String newContent = Math.floor(currentTime - startTime) + "s";
                evalDetail.setEvalScore(newContent);
            }
        }
        if (evalOverview.getEvalTrainingModelId() != null) {
            ModelInfo modelInfo = modelInfoMapper.selectById(evalOverview.getEvalTrainingModelId());
            String status = modelInfo.getStatus();
            if (status.contains("开始时间")) {
                double startTime = Double.parseDouble(status.replace("开始时间", ""));
                // 计算当前时间到开始时间的时间间隔
                double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
                status = Math.floor(currentTime - startTime) + "s";
            }
            ModelInfo modelInfo1 = ModelInfo.builder()
                    .status(status)
                    .modelName(modelInfo.getModelName())
                    .modelId(modelInfo.getModelId())
                    .build();
            evalDetailDecorate.setCurTrainModelInfo(modelInfo1);
        }
        evalDetailDecorate.setEvalDetailList(evalDetailList);
        List<ModelInfo> modelInfoList = modelInfoMapper.selectList(new QueryWrapper<ModelInfo>().isNull("status"));
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
    public int delModel(int modelId) {
        evalDetailMapper.delete(new QueryWrapper<EvalDetail>().eq("model_id", modelId));
        return modelInfoMapper.deleteById(modelId);
    }

    @Override
    public int addNewScore(EvalDetail evalDetail) {
        // 设置当前时间置evalDetail的evalTime字段
        evalDetail.setEvalTime(LocalDateTime.now());
        evalDetailMapper.insert(evalDetail);
        return evalDetail.getEvalDetailId();
    }

    @Override
    public int updateScore(EvalDetail evalDetail) {
        return evalDetailMapper.updateById(evalDetail);
    }

    @Override
    public int updateTrainMsg(ModelInfo modelInfo) {
        return modelInfoMapper.updateById(modelInfo);
    }

    @Override
    public int finishTrain(int evalOverviewId, int modelId) {
        UpdateWrapper<ModelInfo> modelWrapper = new UpdateWrapper<>();
        modelWrapper.eq("model_id", modelId)
                .set("status", null)
                .set("image_name", null)
                .set("model_gen_time", LocalDateTime.now());

        ModelInfo modelInfo = modelInfoMapper.selectById(modelId);
        String modelName = modelInfo.getModelName();
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);

        UpdateWrapper<EvalOverview> overviewWrapper = new UpdateWrapper<>();
        overviewWrapper.eq("eval_overview_id", evalOverviewId)
                .set("eval_algo_ids", evalOverview.getEvalAlgoIds() + "," + modelId)
                .set("eval_algo_names", evalOverview.getEvalAlgoNames() + "," + modelName)
                .set("eval_training_model_id", null);

        evalOverviewMapper.update(null, overviewWrapper);
        modelInfoMapper.update(null, modelWrapper);
        return 1;
    }

    @Override
    public int uploadTestData(UploadTestDataParam param) throws IOException {
        String dataName = param.getDataName();
        File destFile = new File(testDataPath + param.getEvalUserName() + "-" + dataName + ".json");
        MultipartFile multipartFile = param.getFile();
        multipartFile.transferTo(destFile);
        DataInfo dataInfo = new DataInfo();
        dataInfo.setDataName(dataName);
        dataInfo.setDataCreatorId(param.getEvalUserId());
        dataInfo.setDataCreatorName(param.getEvalUserName());
        dataInfo.setDataPath(testDataPath + param.getEvalUserName() + "-" + dataName + ".json");
        dataInfo.setDataGenTime(LocalDateTime.now());
        dataInfo.setDataType("EVAL");
        dataInfo.setDataRelaInfo("");
        dataInfoMapper.insert(dataInfo);
        return dataInfo.getDataId();
    }

    @Override
    public int uploadModel(UploadModelParam param) throws IOException {
        String modelName = param.getModelName();
        File destModelFile = new File(customizeModeModelPath + param.getEvalUserName() + "-" + modelName + ".tar.gz");
        File destEnvFile = new File(customizeModeModelPath + param.getEvalUserName() + "-" + modelName + "-env.tar.gz");
        MultipartFile modelFile = param.getCode();
        MultipartFile envFile = param.getEnv();
        modelFile.transferTo(destModelFile);
        envFile.transferTo(destEnvFile);
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelCreator(param.getEvalUserName());
        modelInfo.setModelCreatorId(param.getEvalUserId());
        modelInfo.setModelGenTime(LocalDateTime.now());
        modelInfo.setModelName(modelName);
        modelInfo.setModelPath(customizeModeModelPath + param.getEvalUserName() + "-" + modelName + ".tar.gz");
        modelInfo.setEnvPath(customizeModeModelPath + param.getEvalUserName() + "-" + modelName + "-env.tar.gz");
        modelInfo.setCmd(param.getCmd());
        modelInfo.setModelVersion("V1");
        modelInfoMapper.insert(modelInfo);
        return modelInfo.getModelId();
    }

    @Override
    public int uploadCheckpoint(UploadCheckpointParam param) throws IOException {
        String checkpointName = param.getCheckpointName();
//        MultipartFile modelFile = param.getFile();
//        File destModelFile = new File(checkpointPath + param.getEvalUserName() + "-" + modelFile.getOriginalFilename());
//        modelFile.transferTo(destModelFile);
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelCreator(param.getEvalUserName());
        modelInfo.setModelCreatorId(param.getEvalUserId());
        modelInfo.setModelGenTime(LocalDateTime.now());
        modelInfo.setModelName(checkpointName);
        modelInfo.setModelPath(checkpointPath + param.getEvalUserName() + "-" + param.getOriginName());
        modelInfo.setModelVersion("V1");
        AlgoInfo algoInfo = algoInfoMapper.selectOne(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", param.getEvalOverviewId()));
        modelInfo.setAlgoId(algoInfo.getAlgoId());
        modelInfoMapper.insert(modelInfo);
        return modelInfo.getModelId();
    }

    @Override
    public int uploadHugModel(UploadHugModelParam param) throws IOException {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelCreator(param.getEvalUserName());
        modelInfo.setModelCreatorId(param.getEvalUserId());
        modelInfo.setModelGenTime(LocalDateTime.now());
        modelInfo.setModelName(param.getHugModelName());
        modelInfo.setModelPath(param.getHugModelPath());
        modelInfo.setAlgoId(null);
        modelInfoMapper.insert(modelInfo);
        return modelInfo.getModelId();
    }

    @Override
    public int uploadTrain(UploadTrainParam param) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelCreator(param.getEvalUserName());
        modelInfo.setModelCreatorId(param.getEvalUserId());
        modelInfo.setModelGenTime(LocalDateTime.now());
        modelInfo.setModelName(param.getModelName());
        modelInfo.setModelTrainDataName(param.getModelTrainDataName());
        modelInfo.setModelPath(customizeModeModelPath + param.getEvalUserName() + "-" + param.getModelName() + param.getSuffix());
        modelInfo.setStatus("待开始");
        modelInfoMapper.insert(modelInfo);
        EvalOverview evalOverview = evalOverviewMapper.selectById(param.getEvalOverviewId());
        evalOverview.setEvalTrainingModelId( modelInfo.getModelId());
        evalOverviewMapper.updateById(evalOverview);
        return modelInfo.getModelId();
    }

    @Override
    public int runTest(RunTestModelParam param) throws IOException {
        String url = customizeInterface;
        ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
        param.setCmd(modelInfo.getCmd());
        param.setEnvPath(modelInfo.getEnvPath());
        param.setModelPath(modelInfo.getModelPath());
        System.out.println(JSON.toJSONString(param));
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }

    @Override
    public int runTestPromote(RunTestModelParam param) throws IOException {
        String url = promoteInterface;
        AlgoInfo algoInfo = algoInfoMapper.selectOne(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", param.getEvalOverviewId()));
        ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
        System.out.println(JSON.toJSONString(algoInfo));
        param.setCmd(algoInfo.getCmd());
        param.setEnvPath(algoInfo.getEnvPath());
        param.setModelPath(algoInfo.getAlgoPath());
        param.setModelFilePath(modelInfo.getModelPath());
        System.out.println(JSON.toJSONString(param));
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }

    @Override
    public int stopTask(StopTaskParam param) throws IOException {
        String url = stopTaskInterface;
        if(param.getTaskType().equals("train")) {
            ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
            modelInfo.setStatus("待开始");
            modelInfoMapper.updateById(modelInfo);
            String imageName = modelInfo.getImageName();
            param.setImageName(imageName);
        }
        else {
            EvalDetail evalDetail = evalDetailMapper.selectById(param.getEvalDetailId());
            String imageName = evalDetail.getImageName();
            param.setImageName(imageName);
            evalDetailMapper.deleteById(param.getEvalDetailId());
        }
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }



    @Override
    public int runTestHug(RunTestModelParam param) throws IOException {
        String url = hgfInterface;
        ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
        param.setModelPath(modelInfo.getModelPath());
        System.out.println(JSON.toJSONString(param));
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }

    @Override
    public int runTrain(RunTestModelParam param) throws IOException {
        String url = trainInterface;
        AlgoInfo algoInfo = algoInfoMapper.selectOne(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", param.getEvalOverviewId()));
        ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
        param.setCheckPointPath(modelInfo.getModelPath());
        System.out.println(JSON.toJSONString(algoInfo));
        param.setCmd(algoInfo.getTrainCmd());
        param.setEnvPath(algoInfo.getEnvPath());
        param.setModelPath(algoInfo.getAlgoPath());
        param.setDataPath(modelInfo.getModelTrainDataName());
        System.out.println(JSON.toJSONString(param));
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        return 1;
    }

    @Override
    public int comparePredictAnswer(int modelId, int testDataId) throws IOException {
        String url = compareInterface;
        EvalDetail evalDetail = evalDetailMapper.selectOne(new QueryWrapper<EvalDetail>().eq("model_id",modelId).eq("eval_data_id", testDataId));
        String modelResultPath = evalDetail.getModelResultPath();
        String testDataPath = dataInfoMapper.selectById(testDataId).getDataPath();
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(PathCollection.builder().modelResultPath(modelResultPath).testDataPath(testDataPath).build()));
        return 1;
    }

    @Override
    public int deleteModel(int modelId, int evalOverviewId) {

        // 获取 EvalOverview 对象
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);

        // 获取 algoIds 和 algoNames，并转换为 ArrayList 方便操作
        List<String> algoIdList = new ArrayList<>(Arrays.asList(evalOverview.getEvalAlgoIds().split(",")));
        List<String> algoNameList = new ArrayList<>(Arrays.asList(evalOverview.getEvalAlgoNames().split(",")));

        // 找到 modelId 的索引并移除对应的元素
        int indexToRemove = algoIdList.indexOf(String.valueOf(modelId));
        if (indexToRemove != -1) {  // 如果找到该 modelId
            algoIdList.remove(indexToRemove);
            algoNameList.remove(indexToRemove);
        }

        // 将列表转换回逗号分隔的字符串
        String algoIds = String.join(",", algoIdList);
        String algoNames = String.join(",", algoNameList);

        // 更新 EvalOverview 对象
        evalOverview.setEvalAlgoIds(algoIds);
        evalOverview.setEvalAlgoNames(algoNames);

        // 打印结果（用于调试）
        System.out.println(algoIds);
        System.out.println(algoNames);
        evalDetailMapper.delete(new QueryWrapper<EvalDetail>().eq("model_id", modelId).eq("eval_overview_id", evalOverviewId));
        evalOverviewMapper.updateById(evalOverview);
        return 1;
    }

    @Override
    public int deleteTrainingModel(int modelId, int evalOverviewId) {
        modelInfoMapper.deleteById(modelId);
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        UpdateWrapper<EvalOverview> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("eval_overview_id", evalOverviewId).set("eval_training_model_id", null);
        evalOverviewMapper.update(null, updateWrapper);
        return 1;
    }

    @Override
    public int deleteTestData(int testDataId, int evalOverviewId) {
        // 获取 EvalOverview 对象
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);

        // 获取 algoIds 和 algoNames，并转换为 ArrayList 方便操作
        List<String> testIdList = new ArrayList<>(Arrays.asList(evalOverview.getEvalTestIds().split(",")));
        List<String> testNameList = new ArrayList<>(Arrays.asList(evalOverview.getEvalTestNames().split(",")));

        // 找到 modelId 的索引并移除对应的元素
        int indexToRemove = testIdList.indexOf(String.valueOf(testDataId));
        if (indexToRemove != -1) {  // 如果找到该 modelId
            testIdList.remove(indexToRemove);
            testNameList.remove(indexToRemove);
        }

        // 将列表转换回逗号分隔的字符串
        String testIds = String.join(",", testIdList);
        String testNames = String.join(",", testNameList);

        // 更新 EvalOverview 对象
        evalOverview.setEvalTestIds(testIds);
        evalOverview.setEvalTestNames(testNames);

        // 打印结果（用于调试）
        System.out.println(testIds);
        System.out.println(testNames);
        evalDetailMapper.delete(new QueryWrapper<EvalDetail>().eq("eval_data_id", testDataId).eq("eval_overview_id", evalOverviewId));
        evalOverviewMapper.updateById(evalOverview);
        return 1;
    }
}
