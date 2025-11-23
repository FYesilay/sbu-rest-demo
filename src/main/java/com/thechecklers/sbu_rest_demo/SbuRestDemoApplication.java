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
interface CoffeeRepository extends CrudRepository<Coffee, String> {}
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

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {

	private List<Coffee> coffees = new ArrayList<>();
	public RestApiDemoController() {
		coffees.addAll(List.of(
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
		return coffees;
	}
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		for (Coffee c: coffees) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}
	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		coffees.add(coffee);
		return coffee;
	}

	@PutMapping("/{id}")
	Coffee putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		int coffeeIndex = -1;
		for (Coffee c: coffees) {
			if (c.getId().equals(id)) {
				coffeeIndex = coffees.indexOf(c);
				coffees.set(coffeeIndex, coffee);
			}
		}


		//return (coffeeIndex == -1) ? postCoffee(coffee) : coffee;
        return (coffeeIndex == -1) ?
                new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED).getBody() :
                new ResponseEntity<>(coffee, HttpStatus.OK).getBody();
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffees.removeIf(c -> c.getId().equals(id));
	}
}