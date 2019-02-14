package reactiveMongoDb.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactiveMongoDb.models.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveRepository extends ReactiveMongoRepository<Person, String> {
	
	Flux<Person> findAllByName(String name);
    Mono<Person> findFirstByName(String name);
}
