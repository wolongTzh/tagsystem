package com.tsinghua.tagsystem.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class EvalDetailDecorate extends EvalOverview {
    private static final long serialVersionUID=1L;
    List<EvalDetail> evalDetailList;

    List<ExistTestData> testDataList;

    List<ExistModel> modelList;

    List<ModelInfo> curTrainModelInfo;

    ModelHelpTag modelHelpTag;

    ModelHelpTag testHelpTag;

    String testHtmlCalculate;

    List<SftLlm> sftMsg;

    String modelTypes;

    List<RunBatchInfo> runBatchInfoList;

    List<WebsiteInfo> websiteInfoList;



    //生成一个构造方法，使用一个EvalOverview对象来构造EvalDetailDecorate
    public EvalDetailDecorate(EvalOverview evalOverview) {
        //使用set来赋值
        this.setEvalOverviewId(evalOverview.getEvalOverviewId());
        this.setEvalTestIds(evalOverview.getEvalTestIds());
        this.setEvalTestNames(evalOverview.getEvalTestNames());
        this.setEvalAlgoIds(evalOverview.getEvalAlgoIds());
        this.setEvalAlgoNames(evalOverview.getEvalAlgoNames());
    }

    //重写它的toString方法，使得其可以输出所继承的EvalOverview类里面的全部属性,还有evalDetailList里面的内容
    @Override
    public String toString() {
        return "EvalDetailDecorate" +
                "evalDetailList=" + evalDetailList +
                ", evalOverviewId=" + getEvalOverviewId() +
                ", evalOverviewName='" + getEvalOverviewName() + '\'' +
                ", evalOverviewIntro='" + getEvalOverviewIntro() + '\'' +
                ", evalTestIds=" + getEvalTestIds() +
                ", evalTestNames=" + getEvalTestNames() +
                ", evalAlgoIds=" + getEvalAlgoIds() +
                ", evalAlgoNames=" + getEvalAlgoNames() +
                ", evalAlreadyRunNum=" +
                 getEvalAlreadyRunNum() + ", evalOverviewTime=" + getEvalOverviewTime() +
                                ", evalOverviewUserId=" + getEvalOverviewUserId() +
                 ", evalOverviewUserName='" + getEvalOverviewUserName() + '\'' +
                                '}';

    }
}
