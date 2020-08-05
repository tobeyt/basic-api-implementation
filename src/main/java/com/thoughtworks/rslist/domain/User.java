package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class User {
    @NotNull
    @Size(max = 8)
    private String userName;
    @Min(18)
    @Max(100)
    private Integer age;
    @NotNull
    private String gender;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    private int vote = 10;

    public User(String userName, Integer age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
