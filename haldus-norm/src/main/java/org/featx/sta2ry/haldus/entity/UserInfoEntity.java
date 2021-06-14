package org.featx.sta2ry.haldus.entity;

import lombok.*;
import org.featx.spec.entity.AbstractUnified;

import java.io.Serial;

/**
 * @author excepts
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoEntity extends AbstractUnified<Long> {
    @Serial
    private static final long serialVersionUID = -1160352953062743575L;

    private String username;

    private String password;

    private Boolean enable;

    private String avatar;

    private String email;

    private Boolean emailVerified;

    private String phone;

    private Boolean phoneVerified;

}
