package com.example.nihongo_trainer.repository;

import com.example.nihongo_trainer.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query("SELECT w FROM Word w WHERE " +
            "LOWER(w.japanese) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(w.translation) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(w.example) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Word> searchWords(@Param("query") String query);
}
