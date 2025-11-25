package com.cohort5.RestBil_System_Backend.Repository;

import com.cohort5.RestBil_System_Backend.Model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for MenuItem data access
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByAvailable(Boolean available);
}
