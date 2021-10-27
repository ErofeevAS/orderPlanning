package group.itechart.orderplanning.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import group.itechart.orderplanning.repository.entity.City;


public interface CityRepository extends JpaRepository<City, Long> {

	Optional<City> findByName(String name);

}
