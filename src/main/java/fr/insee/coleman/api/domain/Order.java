package fr.insee.coleman.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="order")
public class Order {

	@Id
	@Column (name = "id" , updatable = false , nullable = false)
	private Long id;
	
	@Column
	private String status;
	
	@Column
	private int order;

	public Long getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public int getOrder() {
		return order;
	}

	public Order() {
		super();
	}

	public Order(Long id, String status, int order) {
		super();
		this.id = id;
		this.status = status;
		this.order = order;
	}	
}