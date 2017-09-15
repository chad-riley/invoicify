package com.theironyard.invoicify.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.FlatFeeBillingRecord;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

@Controller
@RequestMapping("/billing-records/flat-fees")
public class FlatFeeBillingRecordController {
	
	private BillingRecordRepository recordRepository;
	private CompanyRepository companyRepository;
	
	public FlatFeeBillingRecordController(BillingRecordRepository recordRepository, CompanyRepository companyRepository) {
		this.recordRepository = recordRepository;
		this.companyRepository = companyRepository;
	}

	@PostMapping("")
	public ModelAndView create(FlatFeeBillingRecord record, long clientId, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Company client = companyRepository.findOne(clientId);
		record.setClient(client);
		record.setCreatedBy(user);
		recordRepository.save(record);
		
		return new ModelAndView("redirect:/billing-records");
	}
	
}
