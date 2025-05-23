package be.kdg.integration4.repository;

import be.kdg.integration4.domain.profile.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailIgnoreCase(String email);
    Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE Lower(c.name) LIKE %:name%")
    List<Customer> findAllByNameIgnoreCase(String name);

    @Query("SELECT c FROM Customer c JOIN FETCH c.bikes WHERE c.id = :id")
    Optional<Customer> findByIdWithBikes(Long id);

    Optional<Customer> findByNameIgnoreCase(String name);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.bikes WHERE c.registeredBy.id = :technicianId")
    List<Customer> findByRegisteredBy_Id(Long technicianId);


}
