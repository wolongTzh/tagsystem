package com.tsinghua.tagsystem.controller.utils;

import com.alibaba.fastjson.JSON;
import com.tsinghua.tagsystem.constant.WebConstant;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import com.tsinghua.tagsystem.exception.BusinessException;
import com.tsinghua.tagsystem.model.TestModelParam;
import com.tsinghua.tagsystem.model.WebResInfo;
import com.tsinghua.tagsystem.service.EvalDetailService;
import com.tsinghua.tagsystem.utils.HttpUtil;
import com.tsinghua.tagsystem.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

public class EvalDetailControllerUtil {

    public static void validDisplayParam(int evalOverviewId) {
        if (evalOverviewId == 0) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
    }

    //该方法对参数对象进行参数校验
    public static void validUpdateTableParam(EvalDetailDecorate evalDetailDecorate) {
        if (evalDetailDecorate == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if (StringUtils.isEmpty(evalDetailDecorate.getEvalOverviewId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
        // 对于参数evalTestIds，evalAlgoIds，evalTestNames，evalAlgoNames做相同操作
        if (StringUtils.isEmpty(evalDetailDecorate.getEvalTestIds())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalTestIds不能为空");
        }
        if (StringUtils.isEmpty(evalDetailDecorate.getEvalAlgoIds())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalAlgoIds不能为空");
        }
        if (StringUtils.isEmpty(evalDetailDecorate.getEvalTestNames())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalTestNames不能为空");
        }
        if (StringUtils.isEmpty(evalDetailDecorate.getEvalAlgoNames())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalAlgoNames不能为空");
        }
    }

    public static void validTestModelParam(TestModelParam testModelParam) {
        if (testModelParam == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        // 将判断条件修改为StringUtils.isEmpty()
        if (testModelParam.getEvalOverviewId() == null) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
        if (StringUtils.isEmpty(testModelParam.getModelId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "modelId不能为空");
        }
        // 对于如下参数：modelName，evalDataId，evalDataName，dataPath，evalUserId，evalUserName 做相同操作
        if (StringUtils.isEmpty(testModelParam.getModelName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "modelName不能为空");
        }
        if (StringUtils.isEmpty(testModelParam.getEvalDataId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalDataId不能为空");
        }
        if (StringUtils.isEmpty(testModelParam.getEvalDataName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalDataName不能为空");
        }
        if (StringUtils.isEmpty(testModelParam.getDataPath())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "dataPath不能为空");
        }

        if (StringUtils.isEmpty(testModelParam.getEvalUserId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalUserId不能为空");
        }
        if (StringUtils.isEmpty(testModelParam.getEvalUserName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalUserName不能为空");
        }
    }

    public static void validDeleteTestResultParam(EvalDetail evalDetail) {
        if( evalDetail == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if (StringUtils.isEmpty(evalDetail.getEvalDetailId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalDetailId不能为空");
        }
    }

    public static void validUpdateScoreParam(EvalDetail evalDetail) {
        if (evalDetail == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if (StringUtils.isEmpty(evalDetail.getEvalOverviewId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
        // 对于如下参数：modelId，evalDataId，evalScore，evalScoreDetail，evalUserId，evalUserName
        if (StringUtils.isEmpty(evalDetail.getModelId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "modelId不能为空");
        }
        if (StringUtils.isEmpty(evalDetail.getEvalDataId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalDataId不能为空");
        }
        if (StringUtils.isEmpty(evalDetail.getEvalScore())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalScore不能为空");
        }
        if (StringUtils.isEmpty(evalDetail.getEvalScoreDetail())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalScoreDetail不能为空");
        }
        if (StringUtils.isEmpty(evalDetail.getEvalUserId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalUserId不能为空");
        }
        if (StringUtils.isEmpty(evalDetail.getEvalUserName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalUserName不能为空");
        }
    }
}
