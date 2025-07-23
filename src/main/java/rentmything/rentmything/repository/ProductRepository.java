package rentmything.rentmything.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rentmything.rentmything.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwnerEmail(String email);

}
