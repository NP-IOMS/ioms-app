package com.naaz.ioms.data.access.list_entities;

import com.naaz.ioms.data.access.entities.Users;
import lombok.Getter;

@Getter
public class UsersLight {

    public UsersLight(Users users) {
        this.id = users.getId();
        this.userName = users.getUserName();
        this.userPass = users.getUserPass();
        this.userAccountName = users.getUserAccountName();
        this.userRoleId = users.getUserRole().getId();
    }

    private long id;
    private String userName;
    private String userPass;
    private String userAccountName;
    private long userRoleId;
}
