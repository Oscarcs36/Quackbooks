package com.ds.quackbooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.quackbooks.models.AppRole;
import com.ds.quackbooks.models.Role;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long>  {
    Optional<Role> findByRoleName(AppRole roleName);
}
