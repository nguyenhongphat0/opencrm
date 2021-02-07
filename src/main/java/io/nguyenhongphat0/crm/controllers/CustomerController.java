package io.nguyenhongphat0.crm.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import io.nguyenhongphat0.crm.entities.Customer;
import io.nguyenhongphat0.crm.entities.Resource;
import io.nguyenhongphat0.crm.entities.Service;
import io.nguyenhongphat0.crm.repositories.CustomerRepository;
import io.nguyenhongphat0.crm.repositories.ServiceRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    CustomerRepository customerRepository;

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

    @PostMapping("/{id}/createService")
    public RedirectView createService(@PathVariable long id, Service service) {
        Customer customer = entityManager.getReference(Customer.class, id);
        service.setCustomer(customer);
        serviceRepository.save(service);
        return new RedirectView("/customer/" + id);
    }

    @GetMapping("/removeService/{id}")
    public RedirectView removeService(@PathVariable long id) {
        Service service = serviceRepository.findById(id);
        Long customerId = service.getCustomer().getId();
        serviceRepository.delete(service);
        return new RedirectView("/customer/" + customerId);
    }

    @PostMapping("/{id}/generateWeeklyInvoice")
    public RedirectView generateWeeklyInvoice(@PathVariable long id, Service service) {
        Customer customer = entityManager.getReference(Customer.class, id);
        service.setCustomer(customer);
        service.setType(2);
        String dateString = new SimpleDateFormat("ddMMyyyy").format(new Date());
        service.setName("Invoice # " + dateString);
        serviceRepository.save(service);
        return new RedirectView("/customer/" + id);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable long id, Model model) {
        Customer customer = customerRepository.findById(id);
        model.addAttribute("customer", customer);
        return "/customer/edit";
    }

    @PostMapping("/{id}/updateInformation")
    public RedirectView updateInformation(@PathVariable long id, Customer payload,
            @RequestParam MultipartFile picture) {
        Customer customer = customerRepository.findById(id);
        if (!payload.getName().isEmpty()) {
            customer.setName(payload.getName());
        }
        if (payload.getInformation() != null) {
            customer.setInformation(payload.getInformation());
        }
        if (picture != null) {
            customer.setAvatar(new Resource(picture));
        }
        customer.setUpdatedDate(LocalDateTime.now());
        customerRepository.save(customer);
        return new RedirectView("/customer");
    }
}
