package group.itechart.orderplanning.rest.document;

import group.itechart.orderplanning.rest.document.Geometry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WareHouseDocument {

	private String id;

	private String name;

	private Geometry geometry;

}
