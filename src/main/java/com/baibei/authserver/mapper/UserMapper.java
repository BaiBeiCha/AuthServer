package com.baibei.authserver.mapper;

import com.baibei.authserver.dto.UserDto;
import com.baibei.authserver.entity.Scope;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.ScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.baibei.authserver.entity.Scope.generateScope;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleService roleService;
    private final ScopeService scopeService;

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(roleService.findByName(userDto.getRole()));
        user.setScopes(getScopes(userDto.getScopes()));
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole().getName());
        userDto.setScopes(generateScope(user));
        return userDto;
    }

    private List<Scope> getScopes(String scope) {
        List<Scope> scopes = new ArrayList<>();
        Arrays.asList(scope.split(","))
                .forEach(name -> scopes.add(scopeService.findByName(name)));
        return scopes;
    }
}
