package org.featx.sta2ry.haldus.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoCriteria implements Serializable {
    @Serial
    private static final long serialVersionUID = 4203401260144374188L;

    private String code;

    private String name;

    private Integer type;

    private Boolean enable;

    private String email;

    private String phone;

    private Boolean emailVerified;

    private Boolean phoneVerified;
}
