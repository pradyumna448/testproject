package com.pranav.space_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranav.space_management_system.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
