package com.agonyforge.core.repository;

import com.agonyforge.core.model.Connection;
import com.agonyforge.core.model.Creature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CreatureRepository extends JpaRepository<Creature, UUID> {
    Optional<Creature> findByConnectionSessionUsernameAndConnectionSessionId(String sessionUsername, String sessionId);
    Stream<Creature> findByConnectionIsNotNull();
    Optional<Creature> findByConnection(Connection connection);
}