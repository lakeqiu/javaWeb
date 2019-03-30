package com.lakeqiu.store.domain;

import java.util.Date;

/**
 * 用户信息类
 *
 * @author lakeqiu
 */
public class User {
    /**
     * 用户编号
     */
    private String uid;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String name;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户电话
     */
    private String telephone;
    /**
     * 用户生日
     */
    private Date birthday;
    /**
     * 用户性别
     */
    private String sex;
    /**
     * 用户状态：0未激活，1激活
     */
    private int state;
    /**
     * 激活码
     */
    private String code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getBirthaday() {
        return birthday;
    }

    public void setBirthday(Date birthdy) {
        this.birthday = birthdy;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User() {
    }

    public User(String uid, String username, String password, String name, String email, String telephone, Date birthday, String sex, int state, String code) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.birthday = birthday;
        this.sex = sex;
        this.state = state;
        this.code = code;
    }
}
