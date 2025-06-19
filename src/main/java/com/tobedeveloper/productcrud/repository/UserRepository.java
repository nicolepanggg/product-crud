package com.tobedeveloper.productcrud.repository;
import com.tobedeveloper.productcrud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
