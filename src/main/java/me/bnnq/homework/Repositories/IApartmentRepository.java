package me.bnnq.homework.Repositories;

import me.bnnq.homework.Models.Apartment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IApartmentRepository extends CrudRepository<Apartment, Long>
{
    List<Apartment> findByLandlordIsNull();
    List<Apartment> findByRentalIsNull();
}