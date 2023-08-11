package com.tsinghua.tagsystem.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AddressConfig {

    @Value("${path.taskBase}")
    private String taskBase;

    @Value("${path.finalTag}")
    private String finalTag;

    @Value("${path.subTaskFile}")
    private String subTaskFile;

    @Value("${path.checked}")
    private String checked;

    @Value("${path.escapeCheck}")
    private String escapeCheck;
}
