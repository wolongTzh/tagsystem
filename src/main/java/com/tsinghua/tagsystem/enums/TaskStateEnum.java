package com.tsinghua.tagsystem.enums;

/**
 * 任务状态枚举
 *
 * @author tanzheng
 * @date 2022/12/01
 */
public enum TaskStateEnum {

    INIT(1, "INIT","任务创建完成状态"),
    TAGGING(2, "TAGGING","标注员标注开始，但未完成"),
    UNCHECKED(3, "UNCHECKED","标注员标注完成，但未审核 / 标注员未处理某条数据"),
    CHECKING(4, "CHECKING","审核中，但未完成"),
    FINISHED(5, "FINISHED","任务完成"),
    CHECKED(6, "CHECKED","保留该数据"),
    FAILED(7, "FAILED","删除该数据"),
    EDITED(8, "EDITED","编辑该数据"),
    ;

    private Integer code;

    private String content;

    private String desc;

    TaskStateEnum(Integer code, String content, String desc) {
        this.code = code;
        this.content = content;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    public String getDesc() {
        return desc;
    }
}
