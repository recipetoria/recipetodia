package com.jit.rec.recipetoria.repository;

import com.jit.rec.recipetoria.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByApplicationUser_Id(Long applicationUserId);
}
