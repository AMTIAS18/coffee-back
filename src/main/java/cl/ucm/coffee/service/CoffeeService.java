package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    public CoffeeEntity createCoffee(String name, int price, String description, MultipartFile foto) throws IOException {
        CoffeeEntity coffeeEntity = new CoffeeEntity();
        coffeeEntity.setName(name);
        coffeeEntity.setPrice(price);
        coffeeEntity.setDescription(description);

        if (foto != null && !foto.isEmpty()) {
            coffeeEntity.setImage64(foto.getBytes());
        } else {
            coffeeEntity.setImage64(null);
        }

        return coffeeRepository.save(coffeeEntity);
    }

    public CoffeeEntity updateCoffee(int id, String name, String description, int price, MultipartFile foto) throws IOException {
        Optional<CoffeeEntity> optionalCoffee = coffeeRepository.findById((long) id);
        if (optionalCoffee.isPresent()) {
            CoffeeEntity coffeeEntity = optionalCoffee.get();
            coffeeEntity.setName(name);
            coffeeEntity.setDescription(description);
            coffeeEntity.setPrice(price);

            if (foto != null && !foto.isEmpty()) {
                coffeeEntity.setImage64(foto.getBytes());
            } else {
                coffeeEntity.setImage64(null);
            }

            return coffeeRepository.save(coffeeEntity);
        } else {
            throw new RuntimeException("Coffee not found with id: " + id);
        }
    }

    public Optional<CoffeeEntity> findByName(String name) {
        return coffeeRepository.findByName(name);
    }

    public List<CoffeeEntity> coffeeList() {
        return coffeeRepository.findAll();
    }

    public void deleteCoffee(int idCoffee) {
        coffeeRepository.deleteById((long) idCoffee);
    }
}
