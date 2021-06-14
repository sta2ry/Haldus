package org.featx.sta2ry.haldus.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserInfoSave implements Serializable {

    @Serial
    private static final long serialVersionUID = 2445141624737948621L;

    private String code;

    private String username;

    private String password;

    private String name;

    private String avatar;

    private String email;

    private String phone;
}
