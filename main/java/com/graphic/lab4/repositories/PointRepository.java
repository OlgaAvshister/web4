package com.graphic.lab4.repositories;

import com.graphic.lab4.model.data.Point;
import com.graphic.lab4.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;


public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT u FROM Point u WHERE u.user = ?1 ORDER BY u.id DESC")
    Collection<Point> findAllUserPoints(User user);
}
