package org.ifsul.carhired.api.test;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestEntityRepository extends JpaRepository<TestEntity, UUID> {
}