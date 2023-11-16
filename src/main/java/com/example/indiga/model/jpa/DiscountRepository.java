package com.example.indiga.model.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiscountRepository extends CrudRepository<Discount, Long> {

    @Override
    public List<Discount> findAll();

}
