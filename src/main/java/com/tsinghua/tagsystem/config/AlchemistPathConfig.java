package com.tsinghua.tagsystem.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AlchemistPathConfig {

    @Value("${AlchemistPath.modelCodePath}")
    private String modelCodePath;

    @Value("${AlchemistPath.checkpointPath}")
    private String checkpointPath;

    @Value("${AlchemistPath.testDataPath}")
    private String testDataPath;

    @Value("${AlchemistPath.customizeModeModelPath}")
    private String customizeModeModelPath;

    @Value("${AlchemistPath.trainModelPath}")
    private String trainModelPath;

    @Value("${AlchemistPath.customizeInterface}")
    private String customizeInterface;

    @Value("${AlchemistPath.promoteInterface}")
    private String promoteInterface;

    @Value("${AlchemistPath.trainInterface}")
    private String trainInterface;

    @Value("${AlchemistPath.hgfInterface}")
    private String hgfInterface;

    @Value("${AlchemistPath.compareInterface}")
    private String compareInterface;

    @Value("${AlchemistPath.compareLLMInterface}")
    private String compareLLMInterface;

    @Value("${AlchemistPath.modelHelpInterface}")
    private String modelHelpInterface;

    @Value("${AlchemistPath.compareTripleInterface}")
    private String compareTripleInterface;

    @Value("${AlchemistPath.compareEventInterface}")
    private String compareEventInterface;

    @Value("${AlchemistPath.stopTaskInterface}")
    private String stopTaskInterface;

    @Value("${AlchemistPath.llmTaskInterface}")
    private String llmTaskInterface;

    @Value("${AlchemistPath.vllmTaskInterface}")
    private String vllmTaskInterface;

    @Value("${AlchemistPath.vllmScriptInterface}")
    private String vllmScriptInterface;

    @Value("${AlchemistPath.llmCalculateInterface}")
    private String llmCalculateInterface;

    @Value("${AlchemistPath.grepTimeElapse}")
    private String grepTimeElapse;
}
