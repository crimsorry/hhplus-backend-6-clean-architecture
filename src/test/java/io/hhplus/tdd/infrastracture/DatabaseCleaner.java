package io.hhplus.tdd.infrastracture;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

// JPA 를 사용하고 있기 때문에 Entity Manager 를 이용해 DB 초기화.
@Component
public class DatabaseCleaner implements InitializingBean {

    private final List<String> tableNames = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void afterPropertiesSet() {
        findDatabaseTableNames();
    }

    @SuppressWarnings("unchecked")
    public void findDatabaseTableNames() {
        List<String> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        for (String tableName : tableInfos) {
            tableNames.add(tableName);
        }
    }

    @Transactional
    public void clear() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        entityManager.clear();  // 영속성 컨텍스트를 비움
    }
}