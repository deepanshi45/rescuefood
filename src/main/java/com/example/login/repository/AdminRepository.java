//package com.example.login.repository;
//
//// src/main/java/com/example/foodrescue/repository/AdminRepository.java
//
//
//
//import com.example.login.model.Admin;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface AdminRepository extends JpaRepository<Admin, Long> {
//
//    Optional<Admin> findByUsername(String username);
//
//    Optional<Admin> findByEmail(String email);
//
//    boolean existsByUsername(String username);
//
//    boolean existsByEmail(String email);
//
//    @Query("SELECT a FROM Admin a WHERE a.username = :username")
//    Optional<Admin> getAdminByUsername(@Param("username") String username);
//
//    // Optional: Find by role
//    @Query("SELECT a FROM Admin a WHERE a.adminRole = :role")
//    Optional<Admin> findByAdminRole(@Param("role") String role);
//}
//
//
//package com.example.login.repository;
//
//import com.example.login.model.Admin;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//import java.util.List;
//
//public interface AdminRepository extends JpaRepository<Admin, Long> {
//    Optional<Admin> findByUsername(String username);
//    Optional<Admin> findByEmail(String email);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);
//
//    @Query("SELECT a FROM Admin a WHERE a.adminRole = :role")
//    Optional<Admin> findByAdminRole(@Param("role") String role);
//
//    List<Admin> findAllByAdminRole(String role);
//}
package com.example.login.repository;

import com.example.login.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find by email (for checking duplicates)
    Optional<Admin> findByEmail(String email);


}
