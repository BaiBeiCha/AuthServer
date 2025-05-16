package com.baibei.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.baibei.authserver.entity.Scope;

import java.util.Optional;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Optional<Scope> findByName(String name);
    boolean existsByName(String name);
}
