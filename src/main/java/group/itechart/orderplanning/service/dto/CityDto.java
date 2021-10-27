package group.itechart.orderplanning.service.dto;

import javax.validation.constraints.NotNull;

import group.itechart.orderplanning.repository.entity.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Coordinates coordinates;
}
