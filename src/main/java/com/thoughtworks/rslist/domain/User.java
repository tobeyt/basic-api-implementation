package com.thoughtworks.rslist.domain;

import lombok.Data;

@Data
public class User {
    private String userName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private int vote = 10;
}
