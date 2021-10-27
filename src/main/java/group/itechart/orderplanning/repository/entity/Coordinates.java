package group.itechart.orderplanning.repository.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Coordinates {

	private Double latitude;
	private Double longitude;
}
