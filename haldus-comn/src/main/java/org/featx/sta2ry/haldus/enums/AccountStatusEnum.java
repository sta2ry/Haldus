package org.featx.sta2ry.haldus.enums;

public enum AccountStatusEnum {

    DISABLED(0, "disabled"),
    ENABLED(1, "enabled"), 
    ;

    private int val = 0;

    private String desc = "";

    AccountStatusEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
}
