package com.thechecklers.sbu_rest_demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SbuRestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbuRestDemoApplication.class, args);
	}

}

@Entity
class Coffee{
	@Id
	private String id;
	private String name;

	public Coffee(){};

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee(String name) {
		this(UUID.randomUUID().toString(), name);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

interface CoffeeRepository extends CrudRepository<Coffee, String> {}
@RestController
@RequestMapping("/coffees")
class RestApiDemoController {
	private final CoffeeRepository coffeeRepository;
	private List<Coffee> coffees = new ArrayList<>();
	/*public RestApiDemoController() {
		coffees.addAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));
	}*/
	public RestApiDemoController(CoffeeRepository coffeeRepository, CoffeeRepository coffeeRepository1){

		this.coffeeRepository = coffeeRepository;
		this.coffeeRepository.saveAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));

	}


	//Durch ein Iterieren über die Liste der Kaffees gibt die Methode ein gefülltes Optional
	//<Coffee> zurück, falls sie einen Treffer findet, ansonsten gibt sie ein leeres Optional
	//<Coffee> zurück (falls die angeforderte id in unserer kleinen Gruppe aus Kaffees
	//nicht vorhanden ist):
	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffeeRepository.findAll();
	}
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		return coffeeRepository.findById(id);
	}
	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		return coffeeRepository.save(coffee);
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id,
									 @RequestBody Coffee coffee) {
		return (!coffeeRepository.existsById(id))
				? new ResponseEntity<>(coffeeRepository.save(coffee),
				HttpStatus.CREATED)
				: new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffeeRepository.deleteById(id);
	}
}