package org.featx.sta2ry.haldus.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.featx.spec.enums.BusinessError;
import org.featx.spec.model.BaseResponse;

public interface RoutingHandler extends Handler<RoutingContext> {

    default <T> Handler<T> success(final HttpServerResponse response) {
        return event -> response.end(Json.encodePrettily(BaseResponse.succeeded(event)));
    }

    default Handler<Throwable> error(HttpServerResponse response) {
        return event -> response.end(Json.encodePrettily(BaseResponse.occur(BusinessError.SERVER_ERROR)));
    }
}
