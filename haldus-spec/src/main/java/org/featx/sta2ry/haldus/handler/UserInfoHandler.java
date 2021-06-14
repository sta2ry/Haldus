package org.featx.sta2ry.haldus.handler;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.featx.spec.model.PageResponse;
import org.featx.sta2ry.haldus.model.*;

import java.util.Optional;

import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

public interface UserInfoHandler extends RoutingHandler {
    @Override
    default void handle(final RoutingContext rc) {
        final HttpServerRequest request = rc.request();
        final HttpServerResponse response = rc.response().putHeader("Content-Type", "application/json");
        if (POST.equals(request.method()) && "/user/info".equals(request.path())) {
            request.bodyHandler(it -> {
                UserInfoSave userInfo = it.toJsonObject().mapTo(UserInfoSave.class);
                postUserInfo(userInfo).onSuccess(success(response)).onFailure(error(response));
            });
        } else if (GET.equals(request.method()) && "/user/info".equals(request.path())) {
            String id = Optional.ofNullable(request.getParam("code"))
                    .orElseGet(() -> request.getFormAttribute("code"));
            getUserInfo(id).onSuccess(success(response)).onFailure(error(response));
        }
    }

    Future<UserInfoShow> postUserInfo(UserInfoSave userInfoSave);

    Future<UserInfoDetail> getUserInfo(String userCode);

    Future<Boolean> deleteUserInfo(String userCode);

    Future<PageResponse<UserInfoItem>> page(UserInfoPageRequest userInfoPageRequest);
}
