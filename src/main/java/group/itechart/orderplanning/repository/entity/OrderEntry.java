package group.itechart.orderplanning.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntry {

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column
	private Integer amount;
}
