package rentmything.rentmything.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rentmything.rentmything.Dto.ProductDto;
import rentmything.rentmything.model.Product;
import rentmything.rentmything.model.User;
import rentmything.rentmything.repository.ProductRepository;
import rentmything.rentmything.repository.UserRepository;
import rentmything.rentmything.model.Role;

import java.util.*;

@RestController
@RequestMapping("/rentMyThing")
public class ProductController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto dto) {
        Optional<User> ownerOpt = userRepository.findByEmail(dto.getOwnerEmail());

        if (ownerOpt.isEmpty() || ownerOpt.get().getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPricePerHour(dto.getPricePerHour());
        product.setOwner(ownerOpt.get());

        productRepository.save(product);
        return ResponseEntity.ok("Product added");
    }

    @PostMapping("/requestProductRental")
    public ResponseEntity<?> requestRental(@RequestBody Map<String, String> req) {
        Long productId = Long.parseLong(req.get("productId"));
        String customerEmail = req.get("customerEmail");

        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> customerOpt = userRepository.findByEmail(customerEmail);

        if (productOpt.isEmpty() || customerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        }

        Product product = productOpt.get();
        product.setStatus("REQUESTED");
        productRepository.save(product);

        return ResponseEntity.ok("Rental request submitted");
    }

    // 🧭 Owner Dashboard: Get products by owner email
    @GetMapping("/getOwnerProducts")
    public ResponseEntity<?> getOwnerProducts(@RequestParam String email) {
        Optional<User> ownerOpt = userRepository.findByEmail(email);

        if (ownerOpt.isEmpty() || ownerOpt.get().getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        List<Product> products = productRepository.findByOwnerEmail(email);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Product p : products) {
            Map<String, Object> info = new HashMap<>();
            info.put("productId", p.getId());
            info.put("name", p.getName());
            info.put("description", p.getDescription());
            info.put("pricePerHour", p.getPricePerHour());
            info.put("status", p.getStatus());
            result.add(info);
        }

        return ResponseEntity.ok(result);
    }
}
