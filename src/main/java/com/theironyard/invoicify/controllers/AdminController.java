package com.theironyard.invoicify.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.CompanyRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private CompanyRepository companyRepository;

	public AdminController(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;

	}

	@GetMapping("companies")
	public ModelAndView list() {	
		ModelAndView mv = new ModelAndView("admin/companies");
		mv.addObject(companyRepository);
		mv.addObject("companies", companyRepository.findAll(new Sort(Sort.Direction.ASC, "name")));
		
		return mv;
	}

	@PostMapping("companies")
	public ModelAndView createCompany(Company company) {
		ModelAndView mv = new ModelAndView();
		try {
			companyRepository.save(company);
			mv.setViewName("redirect:/admin/companies");
		} catch (DataIntegrityViolationException dive) {
			mv.setViewName("redirect:/admin/companies");
			mv.addObject("errorMessage", "Cannot create a company with that name");
		}
		return mv;
	}
}
