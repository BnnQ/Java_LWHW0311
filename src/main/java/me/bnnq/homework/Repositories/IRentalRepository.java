package me.bnnq.homework.Repositories;

import me.bnnq.homework.Models.Rental;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRentalRepository extends CrudRepository<Rental, Long>
{
    List<Rental> findByClientIsNull();

    @Query("SELECT AVG(DATEDIFF(rentEndDate, rentStartDate)) FROM Rental")
    Integer findAverageRentalDuration();

    @Query("SELECT MIN(DATEDIFF(rentEndDate, rentStartDate)) FROM Rental")
    Integer findMinimumRentalDuration();

    @Query("SELECT MAX(DATEDIFF(rentEndDate, rentStartDate)) FROM Rental")
    Integer findMaximumRentalDuration();

}