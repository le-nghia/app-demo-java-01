package com.ln.training.app.controller.admin;

import com.ln.training.app.repository.entity.BrandEntity;
import com.ln.training.app.model.ResponseDataModel;
import com.ln.training.app.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin")
public class BrandController {

    IBrandService brandService;

    @Autowired
    public BrandController(IBrandService brandService) {
        this.brandService = brandService;
    }

    @RequestMapping(value = {"/api/find"}, method = RequestMethod.GET)
    public ResponseDataModel findBrandByIdApi(@RequestParam("id") Long brandId){
        return brandService.findBrandByIdApi(brandId);
    }

    @RequestMapping(value = {"/api/findAll/{pageNumber}"}, method = RequestMethod.GET)
    public ResponseDataModel findAllWithPagerApi(@PathVariable("pageNumber") int pageNumber){
        return brandService.findAllWithPageApi(pageNumber);
    }

    @RequestMapping(value = {"/api/add"}, method = RequestMethod.POST)
    public ResponseDataModel addApi(@ModelAttribute BrandEntity brandEntity){
        return brandService.addApi(brandEntity);
    }

    @RequestMapping(value = {"/api/update"}, method = RequestMethod.POST)
    public ResponseDataModel updateApi(@ModelAttribute BrandEntity brandEntity){
        return brandService.updateApi(brandEntity);
    }

    @RequestMapping(value = {"/api/delete/{brandId}"}, method = RequestMethod.DELETE)
    public ResponseDataModel deleteApi(@PathVariable("brandId") Long brandId){
        return brandService.deleteApi(brandId);
    }

    @RequestMapping(value = {"/api/search/{brandName}/{pageNumber}"})
    public ResponseDataModel searchBrand(@PathVariable("brandName") String brandName, @PathVariable("pageNumber") int pageNumber){
        return brandService.searchBrand(brandName, pageNumber);
    }
}
