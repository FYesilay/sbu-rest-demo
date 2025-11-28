package com.thechecklers.sbu_rest_demo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@ConfigurationPropertiesScan
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

	public Coffee(){}

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

@Component
class DataLoader {
	private final CoffeeRepository coffeeRepository;
	public DataLoader(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}
	@PostConstruct
	private void loadData() {
		coffeeRepository.saveAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));
	}
}
/*
@RestController
@RequestMapping("/greeting")
class GreetingControllerValue{
	@Value("${greeting-name: Mirage}")
	private String name;
	@Value("${greeting-coffee: ${greeting-name} is drinking Cafe Ganador}")
	private String coffee;
	@GetMapping
	String getGreeting(){
		return name;
	}
	@GetMapping("/coffee")
	String getNameAndCoffee(){
		return coffee;
	}
}*/
@ConfigurationProperties(prefix="greeting")
class Greeting {
	private String name;
	private String coffee;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoffee() {
		return coffee;
	}
	public void setCoffee(String coffee) {
		this.coffee = coffee;
	}
}

@RestController
@RequestMapping("/greeting")
class GreetingController {
	private final Greeting greeting;
	public GreetingController(Greeting greeting) {
		this.greeting = greeting;
	}
	@GetMapping
	String getGreeting() {
		return greeting.getName();
	}
	@GetMapping("/coffee")
	String getNameAndCoffee() {
		return greeting.getCoffee();
	}
}

class Droid {
	private String id, description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}