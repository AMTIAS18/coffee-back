package cl.ucm.coffee.persitence.repository;

import cl.ucm.coffee.persitence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    List<UserEntity> findByLocked(boolean locked);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.role = :role")
    List<UserEntity> findAllByRole(@Param("role") String role);
}