package com.tsinghua.tagsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsinghua.tagsystem.config.AlchemistPathConfig;
import com.tsinghua.tagsystem.dao.entity.*;
import com.tsinghua.tagsystem.dao.mapper.*;
import com.tsinghua.tagsystem.model.ModelHelpTagDTO;
import com.tsinghua.tagsystem.model.PathCollection;
import com.tsinghua.tagsystem.model.params.*;
import com.tsinghua.tagsystem.service.EvalDetailService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
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
    ModelHelpTagMapper modelHelpTagMapper;

    @Autowired
    LlmTaskMapper llmTaskMapper;

    @Autowired
    DataInfoMapper dataInfoMapper;
    @Autowired
    AlgoInfoMapper algoInfoMapper;

    String modelCodePath;
    String checkpointPath;
    String testDataPath;
    String customizeModeModelPath;
    String trainModelPath;
    String customizeInterface;
    String promoteInterface;
    String trainInterface;
    String hgfInterface;
    String compareInterface;
    String compareTripleInterface;
    String compareEventInterface;
    String modelHelpInterface;
    String stopTaskInterface;
    String grepTimeElapse;
    String vllmTaskInterface;

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
        compareTripleInterface = config.getCompareTripleInterface();
        trainModelPath = config.getTrainModelPath();
        compareEventInterface = config.getCompareEventInterface();
        modelHelpInterface = config.getModelHelpInterface();
        vllmTaskInterface = config.getVllmTaskInterface();
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
//                double startTime = Double.parseDouble(score.replace("开始时间", ""));
//                // 计算当前时间到开始时间的时间间隔
//                double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
//                String newContent = Math.floor(currentTime - startTime) + "s";
                evalDetail.setEvalScore(score.replace("开始时间", ""));
            }
        }
        if (!StringUtils.isEmpty(evalOverview.getEvalTrainingModelId())) {
            String trainingModelIds = evalOverview.getEvalTrainingModelId();
            List<ModelInfo> modelInfoList = new ArrayList<>();
            for(String trainingModelId : trainingModelIds.split(",")) {
                ModelInfo modelInfo = modelInfoMapper.selectById(trainingModelId);
                String status = modelInfo.getStatus();
                if (status.contains("开始时间")) {
//                double startTime = Double.parseDouble(status.replace("开始时间", ""));
//                // 计算当前时间到开始时间的时间间隔
//                double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
//                status = Math.floor(currentTime - startTime) + "s";
                    status = status.replace("开始时间", "");
                }
                ModelInfo modelInfo1 = ModelInfo.builder()
                        .status(status)
                        .modelName(modelInfo.getModelName())
                        .modelId(modelInfo.getModelId())
                        .build();
                modelInfoList.add(modelInfo1);

            }
            evalDetailDecorate.setCurTrainModelInfo(modelInfoList);
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
        ModelHelpTag modelHelpTag = modelHelpTagMapper.selectOne(new QueryWrapper<ModelHelpTag>().eq("eval_overview_id", evalOverviewId).eq("type", "train"));
        evalDetailDecorate.setModelHelpTag(modelHelpTag);
        ModelHelpTag testHelpTag = modelHelpTagMapper.selectOne(new QueryWrapper<ModelHelpTag>().eq("eval_overview_id", evalOverviewId).eq("type", "test"));
        if(!StringUtils.isEmpty(evalOverview.getEvalAutoBuildTestId())) {
            DataInfo dataInfo = dataInfoMapper.selectById(evalOverview.getEvalAutoBuildTestId());
            if(!StringUtils.isEmpty(dataInfo.getDataCurrentInfo())) {
                JSONArray jsonArray = JSONArray.parseArray(dataInfo.getDataCurrentInfo());
                JSONArray jsonArrayDefinition = JSONArray.parseArray(dataInfo.getDataDefinitionInfo());
                JSONArray mergeResult = mergeAndMapTags(jsonArray, jsonArrayDefinition);
                String html = "已经完成";
                if(!checkTags(mergeResult)) {
                    html = generateHtmlTable(mergeAndMapTags(jsonArray, jsonArrayDefinition));
                }
                evalDetailDecorate.setTestHtmlCalculate(html);
            }
        }
        evalDetailDecorate.setTestHelpTag(testHelpTag);
        return evalDetailDecorate;
    }

    Boolean checkTags(JSONArray jsonArray) {
        boolean allTagsSatisfyRequirement = true;

        // 遍历 JSONArray 中的每一个元素
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // 获取已有标签数量和要求数量并转换为整数
            int existingCount = Integer.parseInt(jsonObject.getString("已有标签数量"));
            int requiredCount = Integer.parseInt(jsonObject.getString("要求数量"));

            // 如果已有标签数量小于要求数量，则设置标志为 false 并退出循环
            if (existingCount < requiredCount) {
                allTagsSatisfyRequirement = false;
                break;
            }
        }

        // 输出判断结果
        return allTagsSatisfyRequirement;
    }

    JSONArray mergeAndMapTags(JSONArray jsonArr1, JSONArray jsonArr2) {
        // 创建一个新的 JSONArray 用于存储结果
        JSONArray result = new JSONArray();

        // 遍历第二个 JSONArray
        for (int i = 0; i < jsonArr2.size(); i++) {
            JSONObject jsonObject2 = jsonArr2.getJSONObject(i);
            String tagName2 = jsonObject2.getString("tagName");

            // 创建新的 JSONObject 用于存储映射后的数据
            JSONObject newJsonObject = new JSONObject();

            // 映射原来的字段
            newJsonObject.put("标签名称", tagName2);
            newJsonObject.put("要求数量", jsonObject2.getString("tagNum"));

            // 查找第一个 JSONArray 中是否存在相同的 tagName
            boolean findTag = false;
            for (int j = 0; j < jsonArr1.size(); j++) {
                JSONObject jsonObject1 = jsonArr1.getJSONObject(j);
                String tagName1 = jsonObject1.getString("tagName");

                // 如果 tagName 相同，将 tagNum 添加为 "要求数量"
                if (tagName2.equals(tagName1)) {
                    String tagNum1 = jsonObject1.getString("tagNum");
                    newJsonObject.put("已有标签数量", tagNum1);
                    findTag = true;
                    break;
                }
            }
            // 如果在第一个 JSONArray 中没有找到相同的 tagName，将 "已有标签数量" 设置为 "0"
            if (!findTag) {
                newJsonObject.put("已有标签数量", "0");
            }

            // 将新生成的 JSONObject 加入结果数组
            result.add(newJsonObject);
        }

        // 返回合并和映射后的结果
        return result;
    }

    String generateHtmlTable(JSONArray jsonArray) {
        StringBuilder html = new StringBuilder();

        // 开始表格
        html.append("<table border='1' cellpadding='5' cellspacing='0'>\n");

        // 表头: 假设 JSON 对象的键是列名
        html.append("<tr>");
        JSONObject firstObject = jsonArray.getJSONObject(0);
        for (String key : firstObject.keySet()) {
            html.append("<th>").append(key).append("</th>");
        }
        html.append("</tr>\n");

        // 表格内容
        for (int i = 0; i < jsonArray.size(); i++) {
            html.append("<tr>");
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            for (String key : jsonObject.keySet()) {
                html.append("<td>").append(jsonObject.getString(key)).append("</td>");
            }
            html.append("</tr>\n");
        }

        // 结束表格
        html.append("</table>");

        return html.toString();
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
    public int updateModelHelp(ModelHelpTagDTO modelHelpTag) {
        if(!StringUtils.isEmpty(modelHelpTag.getFee())) {
            Integer evalOverviewId = modelHelpTagMapper.selectById(modelHelpTag.getModelHelpTagId()).getEvalOverviewId();
            EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
            String fee = evalOverview.getFee();
            if(fee == null) {
                fee = "0.0";
            }
            evalOverview.setFee(String.valueOf(Double.parseDouble(modelHelpTag.getFee()) + Double.parseDouble(fee)));
            evalOverviewMapper.updateById(evalOverview);
            modelHelpTag.setFee(null);
        }
        return modelHelpTagMapper.updateById(modelHelpTag);
    }

    @Override
    public int finishModelHelpTagCheckTaskBuild(int modelHelpTagId) {
        ModelHelpTag modelHelpTag = modelHelpTagMapper.selectById(modelHelpTagId);
        modelHelpTag.setOutputPath("已创建标注确认任务");
        return modelHelpTagMapper.updateById(modelHelpTag);
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
        String evalAlgoIds = evalOverview.getEvalAlgoIds();
        String evalAlgoNames = evalOverview.getEvalAlgoNames();
        if(StringUtils.isEmpty(evalAlgoIds)) {
            evalAlgoIds = String.valueOf(modelId);
        }
        else {
            evalAlgoIds = evalAlgoIds + "," + modelId;
        }
        if(StringUtils.isEmpty(evalAlgoNames)) {
            evalAlgoNames = modelName;
        }
        else {
            evalAlgoNames = evalAlgoNames + "," + modelName;
        }

        UpdateWrapper<EvalOverview> overviewWrapper = new UpdateWrapper<>();
        String trainingIds = evalOverview.getEvalTrainingModelId();
        overviewWrapper.eq("eval_overview_id", evalOverviewId)
                .set("eval_algo_ids", evalAlgoIds)
                .set("eval_algo_names", evalAlgoNames)
                .set("eval_training_model_id", trainingIds.replace(String.valueOf(modelId) + ",", ""));

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
        Integer algoId = algoInfoMapper.selectOne(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", param.getEvalOverviewId())).getAlgoId();
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelCreator(param.getEvalUserName());
        modelInfo.setModelCreatorId(param.getEvalUserId());
        modelInfo.setModelGenTime(LocalDateTime.now());
        modelInfo.setModelName(param.getModelName());
        modelInfo.setModelTrainDataName(param.getModelTrainDataName());
        modelInfo.setModelPath(trainModelPath + param.getEvalUserName() + "-" + param.getModelName() + param.getSuffix());
        modelInfo.setStatus("待开始");
        modelInfo.setAlgoId(algoId);
        modelInfoMapper.insert(modelInfo);
        EvalOverview evalOverview = evalOverviewMapper.selectById(param.getEvalOverviewId());
        String oldTrainingModelId = evalOverview.getEvalTrainingModelId();
        if(oldTrainingModelId == null) {
            oldTrainingModelId = "";
        }
        evalOverview.setEvalTrainingModelId(oldTrainingModelId + modelInfo.getModelId() + ",");
        evalOverviewMapper.updateById(evalOverview);
        return modelInfo.getModelId();
    }

    @Override
    public int runTest(RunTestModelParam param) throws IOException {
        String url = customizeInterface;
        ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
        if(modelInfo != null) {
            System.out.println("modelInfo is not null");
            param.setCmd(modelInfo.getCmd());
            param.setEnvPath(modelInfo.getEnvPath());
            param.setModelPath(modelInfo.getModelPath());
            System.out.println(JSON.toJSONString(param));
            HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        }
        else {
            System.out.println("modelInfo is null");
            LlmTask llmTask = llmTaskMapper.selectById(param.getModelId());
            System.out.println(JSON.toJSONString(llmTask));
            param.setLlmTask(llmTask);
            url = vllmTaskInterface;
            System.out.println(JSON.toJSONString(param));
            HttpUtil.sendPostDataByJson(url, JSON.toJSONString(param));
        }
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
    public int runTestModelHelp(RunTestModelParam param) throws IOException {
        String url = modelHelpInterface;
        LlmTask llmTask = llmTaskMapper.selectById(82);
        if(param.getModelHelpType().equals("test")) {
            EvalOverview evalOverview = evalOverviewMapper.selectById(param.getEvalOverviewId());
            DataInfo dataInfo = dataInfoMapper.selectById(evalOverview.getEvalAutoBuildTestId());
            // 创建 ObjectMapper 实例
            ObjectMapper objectMapper = new ObjectMapper();
            // 将字符串转换为 List<Map>，每个 Map 对应一个 JSON 对象
            List<Map<String, String>> definition = objectMapper.readValue(dataInfo.getDataDefinitionInfo(), List.class);
            // TODO:current现在是数据本身不是统计数据，需要转换
            List<Map<String, String>> current = objectMapper.readValue(dataInfo.getDataCurrentInfo(), List.class);
            System.out.println("definition list");
            System.out.println(JSONObject.toJSONString(definition));
            System.out.println("current list");
            System.out.println(JSONObject.toJSONString(current));

            // 遍历 definition
            String mergedKeywords = "";
            String tagCollection = "";
            for (Map<String, String> def : definition) {
                String defTagName = def.get("tagName");
                String defTagNum = def.get("tagNum");
                String defTagKeyword = def.get("tagKeyword");
                tagCollection += defTagName + "、";
                boolean findTag = false;
                // 在 current 中查找与 definition 中相同的 tagName
                for (Map<String, String> cur : current) {
                    String curTagName = cur.get("tagName");
                    String curTagNum = cur.get("tagNum");
                    if(defTagName.equals(curTagName)) {
                        findTag = true;
                    }
                    // 如果 tagName 相同，且 definition 的 tagNum 大于 current 的 tagNum
                    if (defTagName.equals(curTagName) && Integer.parseInt(defTagNum) > Integer.parseInt(curTagNum)) {
                        // 拼接 tagKeyword
                        mergedKeywords += defTagKeyword + "、";
                    }
                }
                if(!findTag) {
                    mergedKeywords += defTagKeyword + "、";
                }
            }
            System.out.println("mergedKeywords");
            System.out.println(mergedKeywords);
            String[] keywords = mergedKeywords.substring(0, mergedKeywords.length()-1).split("、");
            ModelHelpTag modelHelpTag = modelHelpTagMapper.selectById(param.getModelHelpTagId());
            // 读取文件的每一行
            BufferedReader reader = new BufferedReader(new FileReader(modelHelpTag.getDataPath()));
            List<String> filteredLines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                // 检查每行是否包含任意一个关键词
                for (String keyword : keywords) {
                    if (line.contains(keyword)) {
                        // 如果包含某个关键词，则保留这一行
                        filteredLines.add(line);
                        break; // 一旦找到关键词，跳出循环
                    }
                }
            }
            reader.close();

            // 将过滤后的内容写回原文件
            BufferedWriter writer = new BufferedWriter(new FileWriter(modelHelpTag.getDataPath()));
            for (String filteredLine : filteredLines) {
                writer.write(filteredLine);
                writer.newLine();
            }
            writer.close();

            System.out.println("文件过滤并更新成功。");
            llmTask.setFormatDescribe("请从下面这段话中提取出来所有的实体，并且分配在这几个标签中（" + tagCollection.substring(0, tagCollection.length()-1) +"），输出格式为json（[{\"entity\": \"实体\", \"label\": \"对应标签\", \"index\":\"该实体在文中是第几次出现\"}]）：\\n[TEXT]");
        }
        AlgoInfo algoInfo = algoInfoMapper.selectOne(new QueryWrapper<AlgoInfo>().eq("eval_overview_id", param.getEvalOverviewId()));
        if(param.getModelId() != -1) {
            ModelInfo modelInfo = modelInfoMapper.selectById(param.getModelId());
            System.out.println(JSON.toJSONString(algoInfo));
            param.setCmd(algoInfo.getCmd());
            param.setEnvPath(algoInfo.getEnvPath());
            param.setModelPath(algoInfo.getAlgoPath());
            param.setModelFilePath(modelInfo.getModelPath());
            System.out.println(JSON.toJSONString(param));
        }
        else {
            param.setModelType(algoInfo.getModelType());
            //TODO:这块的逻辑不够合理
            param.setLlmTask(llmTask);
            System.out.println(llmTask.getFormatDescribe());
            param.setToken("6306367a14a96d92d3910a33b6d079a8.kGYlp3ommvTt6fFu");
        }
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
            param.setModelPath(modelInfoMapper.selectById(param.getModelId()).getModelPath());
        }
        else if(param.getTaskType().contains("modelHelp")) {
            ModelHelpTag modelHelpTag = modelHelpTagMapper.selectById(param.getModelId());
            modelHelpTag.setOutputPath("待开始");
            modelHelpTagMapper.updateById(modelHelpTag);
            String imageName = modelHelpTag.getImageName();
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
    public String comparePredictAnswer(int modelId, int testDataId) throws IOException {
        String url = compareInterface;
        Integer algoId = modelInfoMapper.selectById(modelId).getAlgoId();
        String modelType = algoInfoMapper.selectById(algoId).getModelType();
        if(modelType.equals("ner")) {
            url = compareInterface;
        }
        else if(modelType.equals("re")) {
            url = compareTripleInterface;
        }
        else if(modelType.equals("event")) {
            url = compareEventInterface;
        }
        EvalDetail evalDetail = evalDetailMapper.selectOne(new QueryWrapper<EvalDetail>().eq("model_id",modelId).eq("eval_data_id", testDataId));
        String modelResultPath = evalDetail.getModelResultPath();
        String testDataPath = dataInfoMapper.selectById(testDataId).getDataPath();
        HttpUtil.sendPostDataByJson(url, JSON.toJSONString(PathCollection.builder().modelResultPath(modelResultPath).testDataPath(testDataPath).build()));
        return modelType;
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
        modelInfoMapper.deleteById(modelId);
        llmTaskMapper.deleteById(modelId);
        return 1;
    }

    @Override
    public int deleteTrainingModel(int modelId, int evalOverviewId) {
        modelInfoMapper.deleteById(modelId);
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        String trainingIds = evalOverview.getEvalTrainingModelId();
        UpdateWrapper<EvalOverview> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("eval_overview_id", evalOverviewId).set("eval_training_model_id", trainingIds.replace(String.valueOf(modelId) + ",", ""));
        evalOverviewMapper.update(null, updateWrapper);
        return 1;
    }

    @Override
    public int deleteModelHelpTag(int modelHelpTagId) {
        modelHelpTagMapper.deleteById(modelHelpTagId);
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
        dataInfoMapper.deleteById(testDataId);
        return 1;
    }

    @Override
    public String getStatus(int evalDetailId) {
        EvalDetail evalDetail = evalDetailMapper.selectById(evalDetailId);
        String score = evalDetail.getEvalScore();
        if (score.contains("开始时间")) {
//                double startTime = Double.parseDouble(score.replace("开始时间", ""));
//                // 计算当前时间到开始时间的时间间隔
//                double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
//                String newContent = Math.floor(currentTime - startTime) + "s";
            score = score.replace("开始时间", "");
        }
        return score;
    }

    @Override
    public String getTrainStatus(int modelId) {
        ModelInfo modelInfo = modelInfoMapper.selectById(modelId);
        String score = modelInfo.getStatus();
        if (score.contains("开始时间")) {
//                double startTime = Double.parseDouble(score.replace("开始时间", ""));
//                // 计算当前时间到开始时间的时间间隔
//                double currentTime = System.currentTimeMillis() / 1000.0;  // 当前时间戳（秒）
//                String newContent = Math.floor(currentTime - startTime) + "s";
            score = score.replace("开始时间", "");
        }
        return score;
    }

    @Override
    public String getModelHelpStatus(int modelHelpId) {
        ModelHelpTag modelHelpTag = modelHelpTagMapper.selectById(modelHelpId);
        String score = modelHelpTag.getOutputPath();
        if (score.contains("开始时间")) {
            score = score.replace("开始时间", "");
        }
        return score;
    }

    @Override
    public int createModelHelpTagTask(ModelHelpTag modelHelpTag) {
        if(modelHelpTag.getModelId() != -1) {
            ModelInfo modelInfo = modelInfoMapper.selectById(modelHelpTag.getModelId());
            modelHelpTag.setModelName(modelInfo.getModelName());
            modelHelpTag.setModelPath(modelInfo.getModelPath());
        }
        modelHelpTag.setOutputPath("待开始");
        modelHelpTagMapper.insert(modelHelpTag);
        return modelHelpTag.getModelHelpTagId();
    }

    @Override
    public int buildAutoTestTask(BuildAutoTestTaskParam buildAutoTestTaskParam) {
        UploadTestDataParam param = buildAutoTestTaskParam.getUploadTestDataParam();
        String dataName = param.getDataName();
        DataInfo dataInfo = new DataInfo();
        dataInfo.setDataName(dataName);
        dataInfo.setDataCreatorId(param.getEvalUserId());
        dataInfo.setDataCreatorName(param.getEvalUserName());
        dataInfo.setDataPath(testDataPath + param.getEvalUserName() + "-AUTO-" + dataName + ".json");
        dataInfo.setDataGenTime(LocalDateTime.now());
        dataInfo.setDataType("EVAL-AUTO");
        dataInfo.setDataRelaInfo("");
        dataInfo.setDataCurrentInfo("[]");
        dataInfo.setDataDefinitionInfo(buildAutoTestTaskParam.getDataDefinitionInfo());
        dataInfoMapper.insert(dataInfo);
        createModelHelpTagTask(buildAutoTestTaskParam.getModelHelpTag());
        EvalOverview evalOverview = evalOverviewMapper.selectById(buildAutoTestTaskParam.getEvalOverviewId());
        evalOverview.setEvalAutoBuildTestId(dataInfo.getDataId());
        evalOverviewMapper.updateById(evalOverview);
        return 1;
    }

    @Override
    public int deleteAutoTestTask(int evalOverviewId) {
        EvalOverview evalOverview = evalOverviewMapper.selectById(evalOverviewId);
        if (evalOverview.getEvalAutoBuildTestId() != null) {
            dataInfoMapper.delete(new QueryWrapper<DataInfo>().eq("data_id", evalOverview.getEvalAutoBuildTestId()));
        }
        UpdateWrapper<EvalOverview> overviewWrapper = new UpdateWrapper<>();
        overviewWrapper.eq("eval_overview_id", evalOverviewId)
                .set("eval_auto_build_test_id", null);
        evalOverviewMapper.update(null, overviewWrapper);
        modelHelpTagMapper.delete(new QueryWrapper<ModelHelpTag>().eq("eval_overview_id", evalOverviewId).eq("type", "test"));
        return 1;
    }

    @Override
    public int uploadAutoTestSource(ModelHelpTag modelHelpTag) {
        modelHelpTag.setOutputPath("待开始");
        modelHelpTagMapper.updateById(modelHelpTag);
        return 1;
    }
}
