package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import cl.ucm.coffee.persitence.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class CoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    public CoffeeEntity createCoffee(String name, int price, String description, MultipartFile foto) throws IOException {
        CoffeeEntity coffeeEntity = new CoffeeEntity();
        coffeeEntity.setName(name);
        coffeeEntity.setPrice(price);
        coffeeEntity.setDescription(description);
        String imageBase64 = Base64.getEncoder().encodeToString(foto.getBytes());
        coffeeEntity.setImage64(imageBase64);

        return coffeeRepository.save(coffeeEntity);
    }
}
