package com.lcwd.electronicstore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    private String userid;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", length=10, nullable = false)
    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name = "user_image_name")
    private String imagename;

    @OneToMany(mappedBy = "user", fetch= FetchType.LAZY, cascade= CascadeType.REMOVE)
    private List<Order> orders=new ArrayList<>();


}
