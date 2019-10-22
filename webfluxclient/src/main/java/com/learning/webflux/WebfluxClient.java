package com.learning.webflux;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@ComponentScan(basePackages = { "com.learning.webflux" })
public class WebfluxClient {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxClient.class, args);
		WebClient client = WebClient.create("http://localhost:8080/");

		client.get().uri("/issuing/stream/accounts").accept(MediaType.TEXT_EVENT_STREAM).retrieve()
				.bodyToFlux(Accout.class)
				.log()
				.subscribeOn(Schedulers.parallel())
				// .subscribe(accout-> System.out.println(accout.getEmbossingName())
				// , err-> System.out.println(err)
				// ,()-> System.out.println("All data has been recieved")
				// ,sub-> sub.request(2)
				// );
				//Slow consumer example
				.subscribe(new Subscriber<Accout>() {
					private Subscription subscription;
					private Integer count = 0;

					@Override
					public void onNext(Accout t) {
						count++;

						if (count >= 2) {
							count = 0;
							subscription.request(2);
						}

					}

					@Override
					public void onSubscribe(Subscription subscription) {
						this.subscription = subscription;
						subscription.request(2);

					}

					@Override
					public void onError(Throwable t) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onComplete() {
						// TODO Auto-generated method stub

					}
				});

	}

}
