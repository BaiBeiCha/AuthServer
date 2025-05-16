package com.baibei.authserver.service.impl;

import com.baibei.authserver.entity.Role;
import com.baibei.authserver.exception.RoleNotExistsException;
import com.baibei.authserver.repository.RoleRepository;
import com.baibei.authserver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotExistsException(name));
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}
