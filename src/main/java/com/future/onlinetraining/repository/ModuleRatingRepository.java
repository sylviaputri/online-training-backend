package com.future.onlinetraining.repository;

import com.future.onlinetraining.entity.ModuleRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRatingRepository extends JpaRepository<ModuleRating, Integer> {

    Page<ModuleRating> findAllByModuleId(int id, Pageable pageable);

}
