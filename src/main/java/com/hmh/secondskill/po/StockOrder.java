package com.hmh.secondskill.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @auther hmh
 * @date 3/6 10:37
 */
@Getter
@Setter
public class StockOrder {

    private Integer id;

    private Integer sid;

    private String name;

    private Date createTime;
}
