package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.RunBatchInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WebsiteService {

   int insertRunBatchTask(RunBatchInfo runBatchInfo);

   int updateRunBatchById(RunBatchInfo runBatchInfo);

   int runBatchTask(RunBatchInfo runBatchInfo) throws IOException;

   int finishRunBatchTask(RunBatchInfo runBatchInfo) throws IOException;
}
