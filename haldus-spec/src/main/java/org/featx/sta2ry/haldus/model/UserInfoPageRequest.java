package org.featx.sta2ry.haldus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.featx.spec.model.PageRequest;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserInfoPageRequest extends PageRequest {
    @Serial
    private static final long serialVersionUID = -6650376611346435322L;

    private String code;

}
