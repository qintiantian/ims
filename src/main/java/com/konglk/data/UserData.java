package com.konglk.data;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserData {
    private String relationshipId;
    private String userId;
    private String username;
    private String nickname;
    private String imgUrl;
    private String cellphone;
    private Integer sex;
    private String country;
    private String city;
    private Integer status;
    private String fpinyin;
}
