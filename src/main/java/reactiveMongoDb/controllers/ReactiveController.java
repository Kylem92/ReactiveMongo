package reactiveMongoDb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactiveMongoDb.models.Person;
import reactiveMongoDb.services.ReactiveService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveController {

	@Autowired
	private ReactiveService service;

	@PostMapping("/save")
	public Mono<Person> save(@RequestBody Person p){	
		return service.save(p);
	}

	@GetMapping("/findbyname")
	public Mono<Person> findFirstByName(@RequestParam("name") String name){	
		return service.findFirstByName(name);
	}

	@GetMapping("/findallbyname")
	public Flux<Person> findAllByName(@RequestParam("name") String name){	
		return  service.findAllByName(name);
	}

	@PostMapping("/addall") //method starts handling the request without waiting for the full payload
	Flux<Person> namesByLastname(@RequestBody Flux<Person> people) {
		return service.insertAll(people);
	}

	@DeleteMapping("/delete")
	public Mono<Boolean> delete(@RequestParam("id") String id) {
		return service.delete(id);
	}
}
