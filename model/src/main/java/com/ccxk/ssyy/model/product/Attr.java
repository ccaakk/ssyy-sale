package com.ccxk.ssyy.model.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Attr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性录入方式：0->手工录入；1->从列表中选取
     */
    private Integer inputType;

    /**
     * 可选值列表[用逗号分隔]
     */
    private String selectList;

    /**
     * 属性分组id
     */
    private Long attrGroupId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标记（0:不可用 1:可用）
     */
    private Integer isDeleted;


}
