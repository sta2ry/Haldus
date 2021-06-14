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
public class UserInfoItem extends UserInfoShow {
    @Serial
    private static final long serialVersionUID = -4191521341310430466L;
}
