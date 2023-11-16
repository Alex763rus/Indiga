package com.example.indiga.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "discount")
public class Discount {

    @Id
    @Column(name = "discount_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    @Column(name = "description")
    private String description;
}
