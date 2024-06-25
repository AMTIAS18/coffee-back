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
        coffeeEntity.setImage64(foto.getBytes());

        return coffeeRepository.save(coffeeEntity);
    }

    public Optional<CoffeeEntity> findByName(String name) {
        return coffeeRepository.findByName(name);
    }

    public List<CoffeeEntity> coffeeList() {
        return coffeeRepository.findAll();
    }

    public CoffeeEntity updateCoffee(CoffeeEntity coffeeEntity) {
        return coffeeRepository.save(coffeeEntity);
    }

    public void deleteCoffee(int idCoffee) {
        coffeeRepository.deleteById((long) idCoffee);
    }
}
