package com.example.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev.vo.User;

// DB 쿼리를 자동으로 작성해주고 매핑해주는 JPA 를 정의 한다.
public interface UserRepository extends JpaRepository<User, Long> {
    User findById (String username);
}

