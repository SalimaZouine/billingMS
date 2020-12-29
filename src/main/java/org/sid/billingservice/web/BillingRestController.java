package org.sid.billingservice.web;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductItemRestClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repository.BillRepository;
import org.sid.billingservice.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingRestController {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private CustomerRestClient customerRestClient;
    @Autowired
    private ProductItemRestClient productItemRestClient;

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id){
        Bill bill= billRepository.findById(id).get();
        System.out.println(bill.getCustomerID());
        Customer customer= customerRestClient.getCustomerById(bill.getCustomerID());
        bill.setCustomer(customer);
        bill.getProductItem().forEach(pi ->{
            Product product= productItemRestClient.getProductById(pi.getProducID());
            //pi.setProduct(product);
            pi.setProductName(product.getName());

        });
        return bill;
    }
}
