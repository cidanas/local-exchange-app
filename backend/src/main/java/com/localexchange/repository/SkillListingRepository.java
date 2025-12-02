package com.localexchange.repository;

import com.localexchange.model.SkillListing;
import com.localexchange.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillListingRepository extends JpaRepository<SkillListing, Long> {
    
    Page<SkillListing> findByTitreContainingIgnoreCase(String search, Pageable pageable);
    
    List<SkillListing> findByOwnerOrderByCreatedAtDesc(User owner);
    
    Page<SkillListing> findByActifTrue(Pageable pageable);
}