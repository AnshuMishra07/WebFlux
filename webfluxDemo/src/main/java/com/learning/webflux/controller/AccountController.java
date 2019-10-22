package com.learning.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.webflux.dao.Account;
import com.learning.webflux.dao.AccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/issuing")
public class AccountController {
	
	@Autowired
	private AccountRepository repo;
	
	
	@GetMapping("/accounts")
	public Flux<Account> getAccount()
	{
		System.out.println("First print statement");
		Flux<Account> accounts=repo.findAll();
		System.out.println(accounts.toString());
		System.out.println("Second print statement");
		return accounts;
				
	}
	
	
	
	
	@GetMapping("/account/{accountId}")
	public Mono<ResponseEntity<Account>> getAccountById(@PathVariable ("accountId") String accounId)
	{
		return repo.findById(accounId).map(savedAccount->ResponseEntity.ok(savedAccount)).log()
				.defaultIfEmpty(ResponseEntity.notFound().build());
				
	}
	
	
	@GetMapping(value="/stream/accounts",produces=org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Account> getAccountByStream()
	{
		return repo.findAll().publishOn(Schedulers.parallel()).log();//.delayElements(Duration.ofSeconds(1));
				
	}
	
	@PostMapping("/account/add")
	public Mono<Account> addAccount(@RequestBody Account account)
	{
		return repo.save(account).log();
	}
	
	@DeleteMapping("/account/{accountId}")
	public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable ("accountId") String accounId)
	{
		return repo.findById(accounId).flatMap(exsitingAccount->
			repo.delete(exsitingAccount).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
		).log().defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PutMapping("/account/{accountId}/update")
	public Mono<ResponseEntity<Account>> updateAccount(@PathVariable ("accountId") String accoounId,@RequestBody Account account)
	{
		return repo.findById(accoounId).flatMap(existingAccount->{
			
			existingAccount.setAmount(account.getAmount());
			existingAccount.setEmbossingName(account.getEmbossingName());
			existingAccount.setName(account.getName());
			return repo.save(existingAccount);
		}).map(updatedAccount-> new ResponseEntity<>(updatedAccount, HttpStatus.OK)).log()
		   .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
	}
	

}
