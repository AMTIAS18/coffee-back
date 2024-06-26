package cl.ucm.coffee.persitence.repository;

import cl.ucm.coffee.persitence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    List<UserEntity> findByLocked(boolean locked);
}