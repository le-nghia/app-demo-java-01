package com.ln.training.app.repository.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product")
@Data
@Setter
@Getter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private Double price;
    @Column(name = "sale_Date")
    private Date saleDate;
    @Column(name = "image")
    private String image;
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "BRAND_ID", referencedColumnName = "BRAND_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private BrandEntity brandEntity;

    @Transient
    private MultipartFile[] imageFiles;

    public BrandEntity getBrandEntity() {
        return brandEntity;
    }

    public void setBrandEntity(BrandEntity brandEntity) {
        this.brandEntity = brandEntity;
    }

    public MultipartFile[] getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(MultipartFile[] imageFiles) {
        this.imageFiles = imageFiles;
    }
}
