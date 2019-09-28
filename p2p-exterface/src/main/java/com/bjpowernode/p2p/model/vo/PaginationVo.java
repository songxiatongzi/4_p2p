package com.bjpowernode.p2p.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/15
 * @description 描述信息
 */
@Data
public class PaginationVo<T> implements Serializable {

    /**
     * total 总记录数
     */
    private Long total;

    /**
     * dataList 每页展示的记录数
     */
    private List<T> dataList;
}
