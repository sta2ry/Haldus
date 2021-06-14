package org.featx.sta2ry.haldus.util;

import org.featx.sta2ry.haldus.criteria.UserInfoCriteria;
import org.featx.sta2ry.haldus.entity.UserInfoEntity;
import org.featx.sta2ry.haldus.model.*;

import java.util.Optional;

public class UserConvertUtil {

    public static UserInfoEntity toUserInfoEntity(UserInfoSave userInfoSave) {
        return Optional.ofNullable(userInfoSave).map(userInfo -> {
            UserInfoEntity entity = new UserInfoEntity();
            entity.setCode(userInfo.getCode());

            entity.setUsername(userInfo.getUsername());
            entity.setPassword(userInfo.getPassword());

            entity.setName(userInfo.getName());

            entity.setEmail(userInfo.getEmail());
            entity.setPhone(userInfo.getPhone());
            entity.setAvatar(userInfo.getAvatar());

            return entity;
        }).orElse(null);
    }

    public static UserInfoCriteria toUserInfoCriteria(UserInfoPageRequest pageRequest) {
        UserInfoCriteria userInfoCriteria = new UserInfoCriteria();
        return userInfoCriteria;
    }


    public static UserInfoShow toUserInfoShow(UserInfoEntity userInfoEntity) {
        return Optional.ofNullable(userInfoEntity).map(entity -> {
            UserInfoShow show = new UserInfoShow();
            show.setCode(entity.getCode());
            //show.setUsername(entity.getUsername());
            show.setName(entity.getName());

            show.setPhone(entity.getPhone());
            show.setEmail(entity.getEmail());
            show.setAvatar(entity.getAvatar());

            show.setCreatedAt(entity.getCreatedAt());
            show.setUpdatedAt(entity.getUpdatedAt());
            return show;
        }).orElse(null);
    }

    public static UserInfoShow toUserInfoShow(UserInfoEntity userInfoEntity, final UserInfoEntity defaultEntity) {

        return Optional.ofNullable(userInfoEntity).map(entity -> {
            UserInfoShow show = new UserInfoShow();
            show.setCode(Optional.ofNullable(entity.getCode()).orElseGet(defaultEntity::getCode));
            //show.setUsername(entity.getUsername());
            show.setName(Optional.ofNullable(entity.getName()).orElseGet(defaultEntity::getName));

            show.setPhone(Optional.ofNullable(entity.getPhone()).orElseGet(defaultEntity::getPhone));
            show.setEmail(Optional.ofNullable(entity.getEmail()).orElseGet(defaultEntity::getEmail));
            show.setAvatar(Optional.ofNullable(entity.getAvatar()).orElseGet(defaultEntity::getAvatar));

            show.setCreatedAt(Optional.ofNullable(entity.getCreatedAt()).orElseGet(defaultEntity::getCreatedAt));
            show.setUpdatedAt(Optional.ofNullable(entity.getUpdatedAt()).orElseGet(defaultEntity::getUpdatedAt));
            return show;
        }).orElse(null);
    }

    public static UserInfoItem toUserInfoItem(UserInfoEntity userInfoEntity) {
        return Optional.ofNullable(userInfoEntity).map(entity -> {
            UserInfoItem item = new UserInfoItem();
            item.setCode(entity.getCode());
            //show.setUsername(entity.getUsername());
            item.setName(entity.getName());

            item.setPhone(entity.getPhone());
            item.setEmail(entity.getEmail());
            item.setAvatar(entity.getAvatar());

            item.setCreatedAt(entity.getCreatedAt());
            item.setUpdatedAt(entity.getUpdatedAt());
            return item;
        }).orElse(null);
    }

    public static UserInfoDetail toUserInfoDetail(UserInfoEntity userInfoEntity) {
        UserInfoDetail userInfoDetail = new UserInfoDetail();
        userInfoDetail.setCode(userInfoEntity.getCode());
//        userInfoDetail.setUsername(userInfoEntity.getUsername());
        userInfoDetail.setName(userInfoEntity.getName());

        userInfoDetail.setPhone(userInfoEntity.getPhone());
        userInfoDetail.setEmail(userInfoEntity.getEmail());
        userInfoDetail.setAvatar(userInfoEntity.getAvatar());

        userInfoDetail.setCreatedAt(userInfoEntity.getCreatedAt());
        userInfoDetail.setUpdatedAt(userInfoEntity.getUpdatedAt());
        return userInfoDetail;
    }

}
