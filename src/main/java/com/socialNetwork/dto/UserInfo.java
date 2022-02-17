package com.socialNetwork.dto;

import com.socialNetwork.entities.user.User;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String name;

    public UserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

}
