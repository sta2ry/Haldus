package org.featx.sta2ry.haldus.entity;

import lombok.*;
import org.featx.spec.entity.AbstractRecord;

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
public class UserSourceEntity extends AbstractRecord<Long> {
    @Serial
    private static final long serialVersionUID = 4821435260059749785L;

    private String userCode;

    private String service;
}
