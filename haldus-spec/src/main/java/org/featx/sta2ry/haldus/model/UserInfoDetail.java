package org.featx.sta2ry.haldus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class UserInfoDetail extends UserInfoShow {
    @Serial
    private static final long serialVersionUID = 4079378016280701741L;
}
