package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.username = u.username\s
      where u.username = :username and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokensByUser(String username);

    Token findByJti(String jti);
}
