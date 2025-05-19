package com.baibei.authserver.service.impl;

import com.baibei.authserver.entity.Scope;
import com.baibei.authserver.exception.ScopeNotExistsException;
import com.baibei.authserver.repository.ScopeRepository;
import com.baibei.authserver.service.ScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository scopeRepository;

    @Override
    public Scope findByName(String name) {
        return scopeRepository.findByName(name)
                .orElseThrow(() -> new ScopeNotExistsException(name));
    }

    @Override
    public Scope save(Scope role) {
        return scopeRepository.save(role);
    }

    @Override
    public boolean existsByName(String name) {
        return scopeRepository.existsByName(name);
    }

    @Override
    public List<Scope> findAll() {
        return scopeRepository.findAll();
    }

    @Override
    public void delete(Scope scope) {
        scopeRepository.delete(scope);
    }
}
