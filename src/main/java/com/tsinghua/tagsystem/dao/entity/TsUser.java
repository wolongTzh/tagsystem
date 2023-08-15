package com.tsinghua.tagsystem.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TsUser {

    private Integer userId;

    private String username;

    private String name;

    private String password;

    private String identity;
}
