package com.example.leapit.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final EntityManager em;

    public Optional<User> findByUsername(String username) {
        try {
            User userPS = em.createQuery("select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(userPS);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }
    }

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(em.find(User.class, id));
    }
}
