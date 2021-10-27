package group.itechart.orderplanning.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WareHouse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column(name = "geo_hash")
	private String geoHash;

	@Column(name = "geo_hash6")
	private String geoHash6;

	@Column(name = "geo_hash5")
	private String geoHash5;

	@Column(name = "geo_hash4")
	private String geoHash4;

	@Column(name = "geo_hash3")
	private String geoHash3;

	@Column(name = "geo_hash2")
	private String geoHash2;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "city_id")
	private City city;

	@OneToMany(mappedBy = "wareHouse")
	private List<StockLevel> stockLevels = new ArrayList<>();

}
