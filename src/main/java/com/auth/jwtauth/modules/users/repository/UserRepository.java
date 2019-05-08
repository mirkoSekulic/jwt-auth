package com.auth.jwtauth.modules.users.repository;

import com.auth.jwtauth.modules.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor{
}
