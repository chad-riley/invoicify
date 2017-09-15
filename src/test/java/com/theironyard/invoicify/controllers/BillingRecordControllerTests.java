package com.theironyard.invoicify.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.BillingRecord;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

public class BillingRecordControllerTests {
	
	private BillingRecordController controller;
	private BillingRecordRepository repo;
	private CompanyRepository companies;
	
	@Before
	public void setup() {
		repo = mock(BillingRecordRepository.class);
		companies = mock(CompanyRepository.class);
		controller = new BillingRecordController(repo, companies);
	}

	@Test
	public void test_list() {
		List<BillingRecord> records = new ArrayList<BillingRecord>();
		when(repo.findAll()).thenReturn(records);
		
		ModelAndView actual = controller.list();
		
		verify(repo).findAll();
		assertThat(actual.getViewName()).isEqualTo("billing-records/list");
		assertThat(actual.getModel().get("records")).isSameAs(records);
	}
	
}
