package com.theironyard.invoicify.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.RateBasedBillingRecord;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

@Controller
@RequestMapping("/billing-records/rate-based")
public class RateBasedBillingRecordController {
	
	private BillingRecordRepository recordRepository;
	private CompanyRepository companyRepository;
	
	public RateBasedBillingRecordController(BillingRecordRepository record, CompanyRepository companyRepository) {
		this.recordRepository = record;
		this.companyRepository = companyRepository;	
	}
	
	@PostMapping("")
	public ModelAndView create(RateBasedBillingRecord record, long clientId, Authentication auth, String description, double quantity) {
		User user = (User) auth.getPrincipal();
		Company client = companyRepository.findOne(clientId);
		record.setClient(client);
		record.setCreatedBy(user);
		record.setQuantity(quantity);
		record.setDescription(description);
		recordRepository.save(record);
	
		return new ModelAndView("redirect:/billing-records");
	
	}
}
