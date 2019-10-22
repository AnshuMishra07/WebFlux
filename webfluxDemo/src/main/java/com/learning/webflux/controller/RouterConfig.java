package com.learning.webflux.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.learning.webflux.dao.Account;
import com.learning.webflux.dao.AccountRepository;

@Component 
public class RouterConfig {
	RouterFunction<?> routes(AccountRepository repo) {
		return nest(path("/issuing"),
				route(GET("/account/{accoutId}"),
						request -> ok().body(repo.findById(request.pathVariable("id")), Account.class))
								.and(route(GET("/accounts"), request -> ok().body(repo.findAll(), Account.class)))
								.and(route(POST("/account/add"),
										request -> request.body(BodyExtractors.toMono(Account.class))
												.doOnNext(repo::save).doOnSuccess(System.out::println).then(ok().build())))

		);
	}

}
