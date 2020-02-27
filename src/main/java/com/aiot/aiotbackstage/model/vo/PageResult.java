package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.common.constant.Constants;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * @ClassName PageResult
 * @Description 分页返回结果封装
 * @Author Administrator
 * @Email 610729719@qq.com
 * @Date 2019/12/3  9:30
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> pageData = Lists.newArrayList();

    private int total = 0;

    private int pageSize = Constants.Page.PAGE_SIZE;

    private int pageNumber = 0;
}
