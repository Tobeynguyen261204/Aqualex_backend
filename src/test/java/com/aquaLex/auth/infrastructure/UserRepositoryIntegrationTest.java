package com.aquaLex.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.aquaLex.auth.domain.User;
import com.aquaLex.auth.domain.UserRole;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByEmail() {
        String email = "integration-" + UUID.randomUUID() + "@example.com";

        User user = new User(
                email,
                "hashed-password",
                "integration-user-" + UUID.randomUUID(),
                "Integration Test User",
                UserRole.USER
        );

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(email)
                .orElseThrow();

        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getUserName()).startsWith("integration-user-");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void shouldReturnTrueWhenEmailAlreadyExists() {
        String email = "existing-" + UUID.randomUUID() + "@example.com";

        User user = new User(
                email,
                "hashed-password",
                "existing-user-" + UUID.randomUUID(),
                "Existing User",
                UserRole.USER
        );

        userRepository.save(user);

        assertThat(userRepository.existsByEmail(email)).isTrue();
    }
}