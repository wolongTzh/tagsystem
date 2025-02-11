package com.tsinghua.tagsystem.model.params;

import com.tsinghua.tagsystem.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CreateTaskParam {

    Integer userId;

    String title;

    Member members;

    File file;

    Integer evalOverviewId;
}
