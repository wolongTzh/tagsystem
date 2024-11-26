package com.tsinghua.tagsystem.controller.utils;

import com.tsinghua.tagsystem.constant.WebConstant;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import com.tsinghua.tagsystem.exception.BusinessException;
import com.tsinghua.tagsystem.model.params.RunTestModelParam;
import org.springframework.util.StringUtils;

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

    public static void validTestModelParam(RunTestModelParam runTestModelParam) {
        if (runTestModelParam == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        // 将判断条件修改为StringUtils.isEmpty()
        if (runTestModelParam.getEvalOverviewId() == null) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
        if (StringUtils.isEmpty(runTestModelParam.getModelId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "modelId不能为空");
        }
        // 对于如下参数：modelName，evalDataId，evalDataName，dataPath，evalUserId，evalUserName 做相同操作
        if (StringUtils.isEmpty(runTestModelParam.getModelName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "modelName不能为空");
        }
        if (StringUtils.isEmpty(runTestModelParam.getEvalDataId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalDataId不能为空");
        }
        if (StringUtils.isEmpty(runTestModelParam.getEvalDataName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalDataName不能为空");
        }
        if (StringUtils.isEmpty(runTestModelParam.getDataPath())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "dataPath不能为空");
        }

        if (StringUtils.isEmpty(runTestModelParam.getEvalUserId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalUserId不能为空");
        }
        if (StringUtils.isEmpty(runTestModelParam.getEvalUserName())) {
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
