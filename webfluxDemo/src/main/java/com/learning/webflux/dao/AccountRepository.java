package com.learning.webflux.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
	public Flux<Account> findAll();
	public Mono<Account> save(Account entity);
	public Mono<Void> deleteById(String Id);

}
