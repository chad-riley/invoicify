package com.theironyard.invoicify.controllers;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserAdditionAspect {

	@ModelAttribute
	public void addUserInformation(Model model, Authentication auth, HttpServletRequest request) {
	    if (auth == null) {
	      model.addAttribute("notUser", true);
	    } else {
	      boolean isAdmin = request.isUserInRole("ADMIN");
	      boolean isClerk = request.isUserInRole("CLERK");
	      boolean isAccountant = request.isUserInRole("ACCOUNTANT");
	      model.addAttribute("user", auth.getPrincipal());
	      model.addAttribute("isAdmin", isAdmin);
	      model.addAttribute("isClerk", isClerk);
	      model.addAttribute("isAccountant", isAccountant);
	    }
	}
}


  