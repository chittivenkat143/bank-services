package com.hcl.services.bank.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.services.bank.domain.Customer;
import com.hcl.services.bank.domain.dto.projection.CustomerView;
import com.hcl.services.bank.domain.dto.projection.CustomerViewOpenProj;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	//List<Customer> findByCustomerType(Integer customerType);

	Optional<CustomerView> findCustomerByCustomerMobile(String mobileNo);
	
	CustomerViewOpenProj findByCustomerEmail(String customerEmail);
	
	List<CustomerView> findByCustomerType(Integer type);
}
