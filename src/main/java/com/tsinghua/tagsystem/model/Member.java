package com.tsinghua.tagsystem.model;

import com.tsinghua.tagsystem.dao.entity.TsUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Member {

    TsUser checkingWorker;

    List<List<TsUser>> taggingWorker;
}
