package com.techie.microservices.cat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_cats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String catNumber;
    private String title;
    private Integer price;
    private String email;
    private String userEmail;
}
