package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import cl.ucm.coffee.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
            @RequestPart(name = "foto", required = false) MultipartFile foto) {

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

    @PutMapping("/updateCoffee")
    public ResponseEntity<?> updateCoffee(
            @RequestParam(name = "idCoffee") int idCoffee,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "price") int price,
            @RequestPart(name = "foto", required = false) MultipartFile foto) {
        try {
            CoffeeEntity updatedCoffee = coffeeService.updateCoffee(idCoffee, name, description, price, foto);
            return ResponseEntity.ok(updatedCoffee);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al actualizar el café: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Café no encontrado: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteCoffee")
    public ResponseEntity<?> deleteCoffee(@RequestParam(name = "idCoffee") int idCoffee) {
        try {
            coffeeService.deleteCoffee(idCoffee);
            return ResponseEntity.ok("Café eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el café: " + e.getMessage());
        }
    }
}
