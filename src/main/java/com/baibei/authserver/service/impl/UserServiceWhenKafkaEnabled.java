package com.baibei.authserver.service.impl;

import com.baibei.authserver.dto.UserAuthDto;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.repository.UserRepository;
import com.baibei.authserver.service.UserService;
import com.baibei.authserver.service.kafka.producer.UserRegistrationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baibei.authserver.entity.Scope.generateScope;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class UserServiceWhenKafkaEnabled implements UserService {

    private final UserRepository userRepository;
    private final UserRegistrationProducer userRegistrationProducer;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public User save(User user) {
        user = userRepository.save(user);

        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setUsername(user.getUsername());
        userAuthDto.setRole(user.getRole().getName());
        userAuthDto.setScope(generateScope(user));

        userRegistrationProducer.sendUserEvent(userAuthDto);

        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
