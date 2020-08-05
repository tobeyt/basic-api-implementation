package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class User {
    @NotNull
    @Size(max = 8)
    @JsonProperty(value = "user_name")
    private String userName;
    @Min(18)
    @Max(100)
    @JsonProperty(value = "user_age")
    private Integer age;
    @NotNull
    @JsonProperty(value = "user_gender")
    private String gender;
    @Email
    @JsonProperty(value = "user_email")
    private String email;
    @Pattern(regexp = "1\\d{10}")
    @JsonProperty(value = "user_phone")
    private String phone;

    private int vote = 10;

    public User(String userName, Integer age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
