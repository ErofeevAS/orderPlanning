package group.itechart.orderplanning.service.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

	@NotNull
	private Long id;
	private String name;
	@NotNull
	private BigDecimal price;
}
