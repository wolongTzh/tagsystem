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

    private String name;

    private int phone;

    private String password;

    private String identity;
}
