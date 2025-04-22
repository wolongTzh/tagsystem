package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.LlmTask;
import com.tsinghua.tagsystem.dao.entity.LlmTaskDecorate;
import com.tsinghua.tagsystem.dao.entity.SftLlm;
import com.tsinghua.tagsystem.model.params.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface SftLLMService {
   public int createSft(SftLlm sftLlm);

   public int runSft(SftLlm sftLlm) throws IOException;

   public int updateSft(SftLlm sftLlm);

   String getLogs(int sftId);
}
