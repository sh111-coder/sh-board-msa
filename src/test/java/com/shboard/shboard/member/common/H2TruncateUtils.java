package com.shboard.shboard.member.common;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class H2TruncateUtils {

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
}
