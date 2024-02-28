package uz.nt.minirailway.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uz.nt.minirailway.domain.entity.train.TrainEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrainRepository extends JpaRepository<TrainEntity, UUID> {
    @Query("delete from trains t where t.id=:id")
    @Modifying
    @Transactional
    void deleteByTrainId(UUID id);
}
