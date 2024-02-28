package uz.nt.minirailway.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uz.nt.minirailway.domain.entity.seat.SeatEntity;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, UUID> {
    Optional<SeatEntity> findBySeatNumber(Integer seatNumber);
    @Modifying
    @Transactional
    @Query("update seats s set s.user=:user, s.passengerName=:passengerName where s.id=:seatId")
    void bookSeat(UserEntity user, String passengerName, UUID seatId);
    List<SeatEntity> findByTrainName(String trainNumber);
    SeatEntity findByTrainIdAndSeatNumber(UUID trainId, int seatNumber);
    List<SeatEntity> findByTrainId(UUID trainId);
}
