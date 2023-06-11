package com.jit.rec.recipetoria.tag;

import com.jit.rec.recipetoria.applicationUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTagsByApplicationUser(ApplicationUser applicationUser);
}
