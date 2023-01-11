package com.example.blog.constants;

/**
 * @author honest
 * @email honest_dong@163.COM
 * @date 2020/6/10
 */
public interface ReturnCode {

    /**
     * 请求成功返回
     */
    Integer success_code = 10200;
    /**
     * 业务错误
     */
    Integer error_code = 10301;

    /**
     * 暂无数据
     */
    Integer no_data_code = 10302;

    /**
     * 数据已存在
     */
    Integer data_is_exist = 10303;

    /**
     * 验证错误
     */
    Integer check_error = 10422;

    /**
     * 输入输出异常
     */
    Integer io_error_code = 10501;

    /**
     * 非法状态异常
     */
    Integer illegal_error_code = 10502;

    /**
     * token过期
     */
    Integer token_expired_code = 10401;

    Integer user_no_code = 10402;

    Integer user_not_find = 10404;



}
