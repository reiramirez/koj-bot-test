package com.example.koj.repository;

import com.example.koj.entity.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Integer> {
    @Query(value = "SELECT * FROM t_knowledge WHERE question LIKE :keyword", nativeQuery = true)
    List<Knowledge> getKnowledgeWithQuestionSearch(@Param("keyword") String keyword);
}
