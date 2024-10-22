package com.tsinghua.tagsystem.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
public class EvalOverviewDecorate extends EvalOverview {
    private static final long serialVersionUID=1L;
    private Integer evalTestNum = 0;
    private Integer evalAlgoNum = 0;

    //生成一个构造方法，使用一个EvalOverview对象来构造EvalOverviewDecorate
    public EvalOverviewDecorate(EvalOverview evalOverview) {
        this.setEvalOverviewId(evalOverview.getEvalOverviewId());
        this.setEvalOverviewName(evalOverview.getEvalOverviewName());
        this.setEvalOverviewIntro(evalOverview.getEvalOverviewIntro());
        this.setEvalAlreadyRunNum(evalOverview.getEvalAlreadyRunNum());
        this.setEvalOverviewTime(evalOverview.getEvalOverviewTime());
    }

    //重写它的toString方法，使得其可以输出所继承的EvalOverview类里面的全部属性
    @Override
    public String toString() {
        return "EvalOverviewDecorate" +
                "evalTestNum=" + evalTestNum +
                ", evalAlgoNum=" + evalAlgoNum +
                ", evalOverviewId=" + evalOverviewId +
                ", evalOverviewName='" + evalOverviewName + '\'' +
                ", evalOverviewIntro='" + evalOverviewIntro + '\'' +
                ", evalTestIds='" + evalTestIds + '\'' +
                ", evalTestNames='" + evalTestNames + '\'' +
                ", evalAlgoIds='" + evalAlgoIds + '\'' +
                ", evalAlgoNames='" + evalAlgoNames + '\'' +
                ", evalAlreadyRunNum=" + evalAlreadyRunNum +
                ", evalOverviewTime=" + evalOverviewTime +
                ", evalOverviewUserId=" + evalOverviewUserId +
        ", evalOverviewUserName='" + evalOverviewUserName + '\'';

    }
}
