package com.ra.session12.repository;

import com.ra.session12.model.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {
    List<Supply> findByIsDeletedFalse();
}