package com.localexchange.repository;

import com.localexchange.model.ItemListing;
import com.localexchange.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemListingRepository extends JpaRepository<ItemListing, Long> {
    
    Page<ItemListing> findByCategorie(String categorie, Pageable pageable);
    
    Page<ItemListing> findByTitreContainingIgnoreCase(String search, Pageable pageable);
    
    Page<ItemListing> findByCategorieAndTitreContainingIgnoreCase(
            String categorie, String search, Pageable pageable);
    
    List<ItemListing> findByOwnerOrderByCreatedAtDesc(User owner);
    
    Page<ItemListing> findByDisponibiliteTrue(Pageable pageable);
}