package com.project.scm.repositories;

import com.project.scm.models.Contacts;
import com.project.scm.models.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contacts, String> {
    List<Contacts> findByUser(User user);


    @Query("SELECT c FROM Contacts c WHERE c.user.id = :userId")
    List<Contacts> findByUserId(@Param("userId") String userId);

    Page<Contacts> findByUser(User user, Pageable pageable);

    Page<Contacts> findByNameContaining(String name, Pageable pageable);

    Page<Contacts> findByEmailContaining(String email, Pageable pageable);

    Page<Contacts> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);


}
