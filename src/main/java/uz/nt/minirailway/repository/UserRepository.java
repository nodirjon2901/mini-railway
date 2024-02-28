package uz.nt.minirailway.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update user_entity u set u.balance=:balance where u.id=:id")
    void fillBalance(Double balance, UUID id);
}
