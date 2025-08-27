package com.ggamja.domain.level.repository;

import com.ggamja.domain.level.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findTopByStartPointLessThanEqualOrderByStartPointDesc(int points);
}