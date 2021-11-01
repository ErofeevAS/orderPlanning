package group.itechart.orderplanning.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class WareHouseDto {

	private Long id;

	private String name;

	private String geoHash;

	private CityDto city;

	//	@JsonIgnore
	//	private List<StockLevel> stockLevels = new ArrayList<>();

}
