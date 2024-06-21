package cl.ucm.coffee.persitence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.ucm.coffee.persitence.entity.CoffeeEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoffeeRepository extends JpaRepository<CoffeeEntity, Long> {
    Optional<CoffeeEntity> findByName(String name);
}

