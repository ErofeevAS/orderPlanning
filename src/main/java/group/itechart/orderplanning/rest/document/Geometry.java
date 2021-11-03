package group.itechart.orderplanning.rest.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geometry {

	private String type;
	private double[] coordinates;

}
