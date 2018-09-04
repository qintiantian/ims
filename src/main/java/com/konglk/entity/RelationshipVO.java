package com.konglk.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ToString
public class RelationshipVO extends BaseEntity {
    private String id;
    private String fromUser;
    private String toUser;
    private Integer relationshipType;
    private String mainUser;
    private Integer status;
    private Long createtime;
    private Long updatetime;
}
