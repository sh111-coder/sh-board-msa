package com.shboard.shboard.member.common;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class RepositoryTest {

    @Autowired
    private EntityManager em;

    @Transactional
    public void truncateAll() {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        final List<String> tableNames = em.getMetamodel().getEntities().stream()
                .map(entityType -> {
                    final String name = entityType.getName();
                    return camelToSnake(name);
                })
                .toList();

        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private String camelToSnake(String str) {
        return str.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }

    @BeforeEach
    void setUp() {
        this.truncateAll();
    }
}
