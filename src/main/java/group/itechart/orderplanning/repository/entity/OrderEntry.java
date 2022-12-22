package group.itechart.orderplanning.repository.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntry extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column
	private Integer amount;

	@ManyToOne
	private WareHouse wareHouse;

	@Column
	private double distance;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Order order;

}
