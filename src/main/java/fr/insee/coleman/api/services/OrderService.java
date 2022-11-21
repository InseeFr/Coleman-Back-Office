package fr.insee.coleman.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.domain.Order;
import fr.insee.coleman.api.repository.OrderRepository;

@Service
public class OrderService {

	static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	OrderRepository orderRepository;
	
	public Order saveAndFlush(Order order) {
		return orderRepository.saveAndFlush(order);
	}

	public Order findByStatus(String status) {
		return orderRepository.findByStatus(status);
	}
}
