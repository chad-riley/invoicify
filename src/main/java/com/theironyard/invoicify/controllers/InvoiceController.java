package com.theironyard.invoicify.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.BillingRecord;
import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.Invoice;
import com.theironyard.invoicify.models.InvoiceLineItem;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.InvoiceLineItemRepository;
import com.theironyard.invoicify.repositories.InvoiceRepository;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private BillingRecordRepository recordRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@PostMapping("/create")
	public ModelAndView createInvoice(Invoice invoice, long clientId, long[] recordIds, Authentication auth) {
		User creator = (User) auth.getPrincipal();
		if (recordIds == null) {
			ModelAndView mv = new ModelAndView("/invoices/step-2");
			mv.addObject("clientId", clientId);
			mv.addObject("records", recordRepository.findByClientIdAndLineItemIsNull(clientId));
			mv.addObject("errorMessage", "Please select at least one billing record");
			
			return mv;
		} else {
			List<BillingRecord> records = recordRepository.findByIdIn(recordIds);
			long nowish = Calendar.getInstance().getTimeInMillis();
			Date now = new Date(nowish);

			List<InvoiceLineItem> items = new ArrayList<InvoiceLineItem>();

			for (BillingRecord record : records) {
				InvoiceLineItem lineItem = new InvoiceLineItem();
				lineItem.setBillingRecord(record);
				lineItem.setCreatedBy(creator);
				lineItem.setCreatedOn(now);
				lineItem.setInvoice(invoice);
				items.add(lineItem);
			}

			invoice.setCompany(companyRepository.findOne(clientId));
			invoice.setCreatedBy(creator);
			invoice.setCreatedOn(now);
			invoice.setLineItems(items);
			invoiceRepository.save(invoice);
		}

		ModelAndView mv = new ModelAndView("redirect:/invoices");
		
		return mv;
	}

	@PostMapping("/new")
	public ModelAndView step2(long clientId, Object recordIds) {
		ModelAndView mv = new ModelAndView("invoices/step-2");
		Company client = companyRepository.findOne(clientId);
		mv.addObject("clientId", clientId);
		mv.addObject("recordIds", recordIds);
		mv.addObject("records", recordRepository.findByClientIdAndLineItemIsNull(clientId));
		
		return mv;
	}

	@GetMapping("")
	public ModelAndView list(Authentication auth) {
		User user = (User) auth.getPrincipal();
		ModelAndView mv = new ModelAndView("invoices/list");
		List<Invoice> invoices = invoiceRepository.findAll();
		mv.addObject("user", user);
		mv.addObject("showTable", invoices.size() > 0);
		mv.addObject("invoices", invoiceRepository.findAll());
		mv.addObject("invoices", invoices);

		return mv;
	}

	@GetMapping("/new")
	public ModelAndView step1() {
		ModelAndView mv = new ModelAndView("invoices/step-1");
		mv.addObject("companies", companyRepository.findAll());
		
		return mv;

	}

}
