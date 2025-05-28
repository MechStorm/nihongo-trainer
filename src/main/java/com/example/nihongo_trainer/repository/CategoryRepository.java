package com.example.nihongo_trainer.repository;

import com.example.nihongo_trainer.dto.CategoryDto;
import com.example.nihongo_trainer.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT new com.example.nihongo_trainer.dto.CategoryDto(c.id, c.name, c.description, COUNT(w.id)) " +
            "FROM Category c LEFT JOIN c.words w " +
            "GROUP BY c.id, c.name, c.description")
    List<CategoryDto> findAllWithWordCount();
}
