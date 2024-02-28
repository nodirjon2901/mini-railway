package uz.nt.minirailway.service.user;

import uz.nt.minirailway.domain.dto.UserDto;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import uz.nt.minirailway.exception.AlreadyExistsException;
import uz.nt.minirailway.exception.NotFoundException;
import uz.nt.minirailway.repository.UserRepository;
import uz.nt.minirailway.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserDto, UserEntity> {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public void save(UserDto createDto) {
        UserEntity user = modelMapper.map(createDto, UserEntity.class);
        try {
            userRepository.save(user);
        } catch (Exception e){
            throw new AlreadyExistsException("Same username already exists!");
        }
    }

    @Override
    public void delete(UUID id) {

    }

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    @Override
    public void update(UserDto createDto, UUID id) {
        try{
            Optional<UserEntity> userEntity = userRepository.findById(id);
            userEntity.get().setUsername(createDto.getUsername());
            userEntity.get().setFullName(createDto.getFullName());
            userEntity.get().setPassword(createDto.getPassword());
            userRepository.save(userEntity.get());
        }catch (Exception e){
            throw new NotFoundException("This user does not exists");
        }
    }

    @Override
    public UserEntity getById(UUID id) {
        if (userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        } else {
            throw new NotFoundException("User Not Found!");
        }
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity login(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new NotFoundException("Wrong username and/or password");
        });
        if (Objects.equals(userEntity.getPassword(),password)){
            return userEntity;
        }
        throw new NotFoundException("Wrong username and/or password");
    }
    public void fillBalance(Double amount, UUID userId) {
        userRepository.fillBalance(amount, userId);
    }
}
