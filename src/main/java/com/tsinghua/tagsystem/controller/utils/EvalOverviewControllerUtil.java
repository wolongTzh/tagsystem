package com.tsinghua.tagsystem.controller.utils;

import com.tsinghua.tagsystem.constant.WebConstant;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.EvalDetailDecorate;
import com.tsinghua.tagsystem.dao.entity.EvalOverview;
import com.tsinghua.tagsystem.enums.BusinessExceptionEnum;
import com.tsinghua.tagsystem.exception.BusinessException;
import com.tsinghua.tagsystem.model.TestModelParam;
import org.springframework.util.StringUtils;

public class EvalOverviewControllerUtil {

    public static void validDisplayParam(int userId) {
        if (userId == 0) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
    }

    //该方法对参数对象进行参数校验
    public static void validAddTaskParam(EvalOverview evalOverview) {
        if (evalOverview == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewName不能为空");
        }
        // 对于参数evalOverviewIntro，evalOverviewUserId，evalOverviewUserName做相同操作
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewIntro())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewIntro不能为空");
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewUserId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewUserId不能为空");
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewUserName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewUserName不能为空");
        }

    }

    public static void validUpdateTaskParam(EvalOverview evalOverview) {
        if (evalOverview == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewName())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewName不能为空");
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewIntro())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewIntro不能为空");
        }
    }

    public static void validDeleteTaskParam(EvalOverview evalOverview) {
        if (evalOverview == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_MISSING_ERROR);
        }
        if (StringUtils.isEmpty(evalOverview.getEvalOverviewId())) {
            throw new BusinessException(WebConstant.RESULT_PARAMETER_ILLEGAL, "evalOverviewId不能为空");
        }
    }
}
