package com.theironyard.invoicify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theironyard.invoicify.models.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Company findByName(String name); 
}
