package group.itechart.orderplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import group.itechart.orderplanning.repository.entity.Client;


public interface ClientRepository extends JpaRepository<Client, Long> {

}
