package group.itechart.orderplanning.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ElementCollection
	private List<OrderEntry> orderEntries = new ArrayList<>();

	@OneToOne
	@JoinColumn(name = "warehouse_id")
	private WareHouse wareHouse;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

}
