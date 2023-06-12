package com.lcwd.electronicstore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_Id")
    private String CategoryId;

    @Column(name = "category_title", length=50, nullable = false)
    private String title;

    @Column(name = "category_description", length= 80)
    private String description;


    private String coverImage;
}
