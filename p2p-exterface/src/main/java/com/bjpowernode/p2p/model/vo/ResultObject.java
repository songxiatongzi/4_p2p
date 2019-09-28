package com.bjpowernode.p2p.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/17
 * @description 描述信息
 */
@Data
public class ResultObject implements Serializable {

    /**
     * errorCode 错误码 success fail
     */
    private String errorCode;

}
