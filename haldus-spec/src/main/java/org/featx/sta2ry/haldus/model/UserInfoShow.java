package org.featx.sta2ry.haldus.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The basic user information to show , info output.
 *
 * @since 2021-06-14 10:14 UTC
 */
@Data
@NoArgsConstructor
public class UserInfoShow implements Serializable {

    @Serial
    private static final long serialVersionUID = 7572493102623714402L;

    private String code;

    private String name;

    private String avatar;

    private String email;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
