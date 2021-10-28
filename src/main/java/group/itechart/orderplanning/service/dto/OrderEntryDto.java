package group.itechart.orderplanning.service.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntryDto {

	@NotNull
	private ProductDto product;
	@NotNull
	private Integer amount;

	private BigDecimal price;

	private String wareHouseName;

	private double distance;

}
