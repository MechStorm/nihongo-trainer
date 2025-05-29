package com.example.nihongo_trainer.repository;

import com.example.nihongo_trainer.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Word> searchWords(@Param("query") String query, Pageable pageable);

    @Query("SELECT w FROM Word w WHERE " +
            "LOWER(w.japanese) LIKE LOWER(CONCAT(:term, '%')) " +
            "OR LOWER(w.translation) LIKE LOWER(CONCAT(:term, '%')) " +
            "OR LOWER(w.example) LIKE LOWER(CONCAT(:term, '%'))")
    List<Word> findWordByTerm(@Param("term") String term);

    Page<Word> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Word> findByCategoryIsNull(Pageable pageable);
    Page<Word> findAll(Pageable pageable);
}
