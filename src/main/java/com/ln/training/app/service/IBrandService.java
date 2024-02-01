package com.ln.training.app.service;

import com.ln.training.app.repository.entity.BrandEntity;
import com.ln.training.app.model.ResponseDataModel;

import java.util.List;

public interface IBrandService {
    /**
     * Display all brands
     */
    List<BrandEntity> getAll();
    /**
     * Find by name brand
     */
    BrandEntity findByBrandName(String brandName);
    /**
     * Display all brand.
     */
    ResponseDataModel findAllWithPageApi(int pageNumber);
    /**
     *Add brand by brandEntity
     */
    ResponseDataModel addApi(BrandEntity brandEntity);
    /**
     *Update brand by brandEntity
     */
    ResponseDataModel updateApi(BrandEntity brandEntity);
    /**
     * Delete by Id
     */
    ResponseDataModel deleteApi(Long brandId);
    /**
     * Search by ib
     */
    ResponseDataModel findBrandByIdApi(Long brandId);
    /**
     *Search brand by name
     */
    ResponseDataModel searchBrand(String brandName, int pageNumber);

}
