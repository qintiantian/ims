package com.konglk.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class RelationshipVO extends BaseEntity {
    private String id;
    private String fromUser;
    private String toUser;
    private Integer relationshipType;
    private String mainUser;
    private Integer status;
    private String greet;
    private Long createtime;
    private Long updatetime;
    private String remark;
}
