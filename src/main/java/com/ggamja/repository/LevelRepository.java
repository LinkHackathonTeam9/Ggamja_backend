package com.ggamja.repository;

import com.ggamja.domain.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findTopByStartPointLessThanEqualOrderByStartPointDesc(int points);
}