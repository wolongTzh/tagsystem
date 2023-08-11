package com.tsinghua.tagsystem.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CreateTaskParam {

    Integer userId;

    String title;

    List<List<Integer>> members;

    File file;

    MultipartFile rawFile;
}
