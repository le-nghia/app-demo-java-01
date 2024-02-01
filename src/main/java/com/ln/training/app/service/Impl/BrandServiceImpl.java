package com.ln.training.app.service.Impl;

import com.ln.training.app.common.constant.Constants;
import com.ln.training.app.common.util.FileHelper;
import com.ln.training.app.repository.entity.BrandEntity;
import com.ln.training.app.model.PagerModel;
import com.ln.training.app.model.ResponseDataModel;
import com.ln.training.app.repository.BrandRepository;
import com.ln.training.app.service.IBrandService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LeNghia
 */
@Service
@Transactional
public class BrandServiceImpl implements IBrandService {

    private static final Logger LOG =  LoggerFactory.getLogger(BrandServiceImpl.class);

    @Value("${parent.folder.images.brand}")
    private String brandLogoFolderPath;

    BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandEntity> getAll() {
        return brandRepository.findAll(Sort.by(Sort.Direction.DESC,"brandId"));
    }

    @Override
    public BrandEntity findByBrandName(String brandName) {
        return brandRepository.findByBrandName(brandName);
    }

    @Override
    public ResponseDataModel findAllWithPageApi(int pageNumber) {
        int responseCode = Constants.RESULT_CD_FAIL;
        String responseMsg = StringUtils.EMPTY;
        Map<String,Object> responseMap = new HashMap<>();
        try {
            Sort sortInfo = Sort.by(Sort.Direction.DESC, "brandId");
            Pageable pageable = PageRequest.of(pageNumber - 1, Constants.PAGE_SIZE, sortInfo);
            Page<BrandEntity>  brandEntityPage = brandRepository.findAll(pageable);
            responseMap.put("brandsList", brandEntityPage.getContent());
            responseMap.put("paginationInfo", new PagerModel(pageNumber,brandEntityPage.getTotalPages()));
            responseCode = Constants.RESULT_CD_SUCCESS;
        }catch (Exception e){
            responseMsg = e.getMessage();
            LOG.error("Error when get all brand !" , e);
        }
        return new ResponseDataModel(responseCode, responseMsg, responseMap);
    }

    @Override
    public ResponseDataModel addApi(BrandEntity brandEntity) {
        int responseCode = Constants.RESULT_CD_FAIL;
        String responseMsg;
        try {
            if (findByBrandName(brandEntity.getBrandName()) != null){
                responseMsg = " Brand Name is duplicated";
                responseCode = Constants.RESULT_CD_DUPLs;

            }else {
                MultipartFile[] logoFiles = brandEntity.getLogoFiles();
                if (logoFiles != null && logoFiles[0].getSize() > 0){
                    String imagePath = FileHelper.addNewFile(brandLogoFolderPath, logoFiles);
                    brandEntity.setLogo(imagePath);
                }
                brandRepository.saveAndFlush(brandEntity);
                responseMsg = "Brand is added successfully";
                responseCode = Constants.RESULT_CD_SUCCESS;
            }
        }catch (Exception e){
            responseMsg = "Error when adding brand";
            LOG.error("Error when adding brand");
        }
        return new ResponseDataModel(responseCode, responseMsg);
    }

    @Override
    public ResponseDataModel updateApi(BrandEntity brandEntity) {

        int responseCode = Constants.RESULT_CD_FAIL;
        String responseMsg;

        try {
            BrandEntity duplicatedBrand = brandRepository.findByBrandNameAndBrandIdNot(brandEntity.getBrandName(),brandEntity.getBrandId());

            // check if brand name existed
            if (duplicatedBrand != null){
                responseMsg = "Brand name is duplicated";
                responseCode = Constants.RESULT_CD_DUPLs;
            }else{
                MultipartFile[] logoFiles = brandEntity.getLogoFiles();
                if (logoFiles != null && logoFiles[0].getSize() > 0){
                    String imagePath = FileHelper.editFile(brandLogoFolderPath, logoFiles, brandEntity.getLogo());
                    brandEntity.setLogo(imagePath);
                }
                brandRepository.saveAndFlush(brandEntity);
                responseMsg = "Brand is updated successfully";
                responseCode = Constants.RESULT_CD_SUCCESS;
            }
        }catch (Exception e){
            responseMsg = "Error when updating brand!!";
            LOG.error("Error when updating brand: ", e);
        }
        return new ResponseDataModel(responseCode, responseMsg);
    }

    @Override
    public ResponseDataModel deleteApi(Long brandId) {
        int responseCode = Constants.RESULT_CD_FAIL;
        String responseMsg = StringUtils.EMPTY;
        BrandEntity brandEntity = brandRepository.findByBrandId(brandId);
        try {
            if(brandEntity != null){
                brandRepository.deleteById(brandId);
                brandRepository.flush();

                //Remove logo of brand from store folder
                FileHelper.deleteFile(brandEntity.getLogo());
                responseMsg = "Brand is deleted successfully";
                responseCode = Constants.RESULT_CD_SUCCESS;
            }
        } catch (Exception e) {
            responseMsg = "Error when deleting brand!!";
            LOG.error("Error when deleting brand: ",e);
            e.printStackTrace();
        }
        return new ResponseDataModel(responseCode, responseMsg);
    }

    @Override
    public ResponseDataModel findBrandByIdApi(Long brandId) {
        int responseCode = Constants.RESULT_CD_FAIL;
        String responseMsg = StringUtils.EMPTY;
        BrandEntity brandEntity = null;
        try {
            brandEntity = brandRepository.findByBrandId(brandId);
            if (brandEntity != null){
                responseCode = Constants.RESULT_CD_SUCCESS;
            }
        }catch (Exception e){
            responseMsg = "Error when finding brand by id!! ";
            LOG.error("Error when finding brand by id: ", e);
        }
        return new ResponseDataModel(responseCode, responseMsg, brandEntity);
    }

    @Override
    public ResponseDataModel searchBrand(String brandName, int pageNumber) {
        int responseCode = Constants.RESULT_CD_FAIL;
        String responseMsg;
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Sort sortList = Sort.by(Sort.Direction.DESC, "brandId");
            Pageable pageable = PageRequest.of(pageNumber - 1, Constants.PAGE_SIZE, sortList);
            Page<BrandEntity> brandEntityPage = brandRepository.findByBrandNameLike("%" + brandName + "%", pageable);
            responseMap.put("brandsList",brandEntityPage.getContent());
            responseMap.put("paginationList", new PagerModel(pageNumber, brandEntityPage.getTotalPages()));
            responseCode = Constants.RESULT_CD_SUCCESS;
            if (brandEntityPage.getTotalElements() > 0){
                responseMsg = "The number of brand found is "+ brandEntityPage.getTotalElements() + " brand";
            }else {
                responseMsg = " the "+ brandName + " is not exist!!";
            }
        }catch (Exception e){
            responseMsg = e.getMessage();
            LOG.error("Search brand name failed: ", e);
        }
        return new ResponseDataModel(responseCode, responseMsg, responseMap);
    }
}
