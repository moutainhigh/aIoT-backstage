package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

/**
 * @Description Token出参
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 14:12
 */
@Data
public class TokenVo {

    private String token;

    public TokenVo(String token) {
        this.token = token;
    }
}
