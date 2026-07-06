package org.featx.sta2ry.haldus.util;

import org.featx.sta2ry.haldus.criteria.AccountCriteria;
import org.featx.sta2ry.haldus.entity.AccountEntity;
import org.featx.sta2ry.haldus.model.*;

import java.util.Optional;

public class AccountConvertUtil {

    public static AccountEntity toAccountEntity(AccountSave accountSave) {
        return Optional.ofNullable(accountSave).map(account -> {
            AccountEntity entity = new AccountEntity();
            entity.setCode(account.getCode());

            entity.setUsername(account.getUsername());
            entity.setPassword(account.getPassword());

            entity.setName(account.getName());

            entity.setEmail(account.getEmail());
            entity.setPhone(account.getPhone());
            entity.setAvatar(account.getAvatar());

            return entity;
        }).orElse(null);
    }

    public static AccountCriteria toAccountCriteria(AccountPageRequest pageRequest) {
        AccountCriteria accountCriteria = new AccountCriteria();
        return accountCriteria;
    }


    public static AccountShow toAccountShow(AccountEntity accountEntity) {
        return Optional.ofNullable(accountEntity).map(entity -> {
            AccountShow show = new AccountShow();
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

    public static AccountShow toAccountShow(AccountEntity accountEntity, final AccountEntity defaultEntity) {

        return Optional.ofNullable(accountEntity).map(entity -> {
            AccountShow show = new AccountShow();
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

    public static AccountItem toAccountItem(AccountEntity accountEntity) {
        return Optional.ofNullable(accountEntity).map(entity -> {
            AccountItem item = new AccountItem();
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

    public static AccountDetail toAccountDetail(AccountEntity accountEntity) {
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setCode(accountEntity.getCode());
//        accountDetail.setUsername(accountEntity.getUsername());
        accountDetail.setName(accountEntity.getName());

        accountDetail.setPhone(accountEntity.getPhone());
        accountDetail.setEmail(accountEntity.getEmail());
        accountDetail.setAvatar(accountEntity.getAvatar());

        accountDetail.setCreatedAt(accountEntity.getCreatedAt());
        accountDetail.setUpdatedAt(accountEntity.getUpdatedAt());
        return accountDetail;
    }

}
