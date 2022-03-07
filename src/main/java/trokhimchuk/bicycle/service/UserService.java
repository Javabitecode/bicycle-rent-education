package trokhimchuk.bicycle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import trokhimchuk.bicycle.Entity.Role;
import trokhimchuk.bicycle.Entity.UserEntity;
import trokhimchuk.bicycle.exception.UserAlreadyExistException;
import trokhimchuk.bicycle.exception.UserNotFoundException;
import trokhimchuk.bicycle.model.User;
import trokhimchuk.bicycle.repo.UserRepository;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким именем существует");
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return userRepository.save(user);
    }

    public User getOne(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).get();
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(user);
    }

    public Long delete(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         return userRepository.findByUsername(username);
        }

}
