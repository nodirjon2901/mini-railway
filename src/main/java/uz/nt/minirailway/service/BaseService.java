package uz.nt.minirailway.service;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BaseService<CD, E> {
    void save(CD createDto);
    void delete(UUID id);
    void delete(E e);
    void update(CD createDto, UUID id);
    E getById(UUID id);
    List<E> getAll();
}
