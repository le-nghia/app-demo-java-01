package com.ln.training.app.repository;

import com.ln.training.app.repository.entity.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    BrandEntity findByBrandId(Long brandId);
    BrandEntity findByBrandName(String brandName);
    BrandEntity findByBrandNameAndBrandIdNot(String brandName, Long brandId);
    Page<BrandEntity> findByBrandNameLike(String brandName, Pageable pageable);
}
