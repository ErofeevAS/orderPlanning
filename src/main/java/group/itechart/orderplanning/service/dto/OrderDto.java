package group.itechart.orderplanning.service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

	private Long id;
	@NotNull
	private Long clientId;
	private List<OrderEntryDto> orderEntries = new ArrayList<>();
	private BigDecimal totalPrice;
	private BigDecimal distance;
}
