package group.itechart.orderplanning.service.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

	private Long id;
	@NotNull
	private String name;
	@NotNull
	private CityDto city;
}
