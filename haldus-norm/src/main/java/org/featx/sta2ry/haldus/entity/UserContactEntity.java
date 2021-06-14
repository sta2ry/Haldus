package org.featx.sta2ry.haldus.entity;

import lombok.*;
import org.featx.spec.entity.AbstractUnified;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContactEntity extends AbstractUnified<Long> {
    @Serial
    private static final long serialVersionUID = -7355576738347269375L;

    private String userCode;

    private String contact;

    private String phone;

    private Boolean phoneVerified;

    private Boolean defauld;

    private String distinct;

    private String address;

    private String comment;
}
