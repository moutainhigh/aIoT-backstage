package com.aiot.aiotbackstage.common.constant;

/**
 * 全局常量
 */
public interface Constants {

    public static final String RTU_LAST_TIME = "RTU-LAST-TIME";

    /**
     * 默认分页信息
     */
    interface Page {
        /**
         * 默认分页大小
         */
        int PAGE_SIZE = 10;

        /**
         * 最大分页大小
         */
        int MAX_PAGE_SIZE = 1000;
    }

    /**
     * 数据库相关
     */
    interface Sql {
        /**
         * 批量插入最大限制数
         */
        int MAX_BATCH_INSERT_SIZE = 1000;
    }
}
