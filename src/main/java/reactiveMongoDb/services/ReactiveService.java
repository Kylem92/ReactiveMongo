package reactiveMongoDb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactiveMongoDb.models.Person;
import reactiveMongoDb.repositories.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveService {

	@Autowired
	private ReactiveRepository repo;

	public Mono<Person> save(Person p) {
		return repo.save(p);
	}

	public Mono<Person> findFirstByName(String name){
		return repo.findFirstByName(name);
	}

	public Flux<Person> findAllByName(String name){ //data is pushed to the consumer as it's available
		return repo.findAllByName(name).doOnNext(person -> {
			person.setActive(true);
			repo.save(person).subscribe(System.out::println);		
		}).flatMap(person -> Flux.just(person));
	}

	public Flux<Person> insertAll(Flux<Person> people) {
		return repo.insert(people);
	}

	public Mono<Boolean> delete(String id) {
		return repo.findById(id).doOnSuccess(person -> {
			person.setActive(false);
			repo.save(person).subscribe(System.out::println);
		}).flatMap(person -> Mono.just(Boolean.TRUE));
	}
}
