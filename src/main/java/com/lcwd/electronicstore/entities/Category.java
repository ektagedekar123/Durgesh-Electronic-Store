package com.lcwd.electronicstore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity{

    @Id
    @Column(name = "category_Id")
    private String CategoryId;

    @Column(name = "category_title", length=50, nullable = false)
    private String title;

    @Column(name = "category_description", length= 80)
    private String description;

    private String coverImage;

    @OneToMany(mappedBy = "category", cascade= CascadeType.ALL, fetch= FetchType.LAZY )
    private List<Product> products=new ArrayList<>();
}
