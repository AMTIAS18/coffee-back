package cl.ucm.coffee.web.controller;


import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import cl.ucm.coffee.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coffee")
public class CoffeeController {

    @Autowired
    private CoffeeService coffeeService;

    @Autowired
    private CoffeeRepository coffeeRepository;

    @PostMapping("/create")
    public ResponseEntity<CoffeeEntity> create(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "price") int price,
            @RequestParam(name = "desc") String description,
            @RequestParam(name = "foto") MultipartFile foto) {

        try {
            CoffeeEntity createdCoffee = coffeeService.createCoffee(name, price, description, foto);
            return ResponseEntity.ok(createdCoffee);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/coffeeList")
    public ResponseEntity<?> coffeeList() {
        try {
            List<CoffeeEntity> coffeeList = coffeeRepository.findAll();
            return ResponseEntity.ok(coffeeList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener la lista de cafés: " + e.getMessage());
        }
    }


    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> coffe(){
        Map map = new HashMap();
        map.put("coffee", "Coffees Post:)");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/findByName")
    public ResponseEntity<?> findByName(@RequestParam(name = "name") String name) {
        try {
            Optional<CoffeeEntity> coffeeOptional = coffeeService.findByName(name);
            if (coffeeOptional.isPresent()) {
                return ResponseEntity.ok(coffeeOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar el café por nombre: " + e.getMessage());
        }
    }



}
