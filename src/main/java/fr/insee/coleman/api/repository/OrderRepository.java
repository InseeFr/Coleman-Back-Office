package fr.insee.coleman.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.domain.Order;

@Service
public interface OrderRepository extends JpaRepository<Order, String> {

    Order findByStatus(String status);
}