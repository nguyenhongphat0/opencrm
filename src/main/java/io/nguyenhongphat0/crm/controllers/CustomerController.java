package io.nguyenhongphat0.crm.controllers;

import io.nguyenhongphat0.crm.entities.Customer;
import io.nguyenhongphat0.crm.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired CustomerRepository customerRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("customers", customerRepository.findAllByOrderByUpdatedDateDesc());
        return "customer/list";
    }

    @PostMapping("/create")
    public RedirectView create(Customer customer) {
        customerRepository.save(customer);
        return new RedirectView("/customer");
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        Customer customer = customerRepository.findById(id);
        model.addAttribute("customer", customer);
        return "customer/detail";
    }
}