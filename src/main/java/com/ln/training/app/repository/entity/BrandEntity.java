package com.ln.training.app.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "brand")
@Data
@AllArgsConstructor
@Setter
@Getter
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "brand_name", nullable = true, length = 100)
    private String brandName;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "logo", nullable = true)
    private String logo;

    @Transient
    private MultipartFile[] logoFiles;

    @JsonIgnore
    @OneToMany(mappedBy = "brandEntity", fetch = FetchType.LAZY)
    private Set<ProductEntity> productEntities;

    public BrandEntity() {

    }
}
