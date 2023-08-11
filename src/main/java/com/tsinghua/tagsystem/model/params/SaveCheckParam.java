package com.tsinghua.tagsystem.model.params;

import com.tsinghua.tagsystem.model.CheckAtom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SaveCheckParam {

    String taskId;

    Integer uncheckedNum;

    List<CheckAtom> checkList;
}
