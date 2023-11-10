package me.bnnq.homework.Repositories;

import me.bnnq.homework.Models.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IClientRepository extends CrudRepository<Client, Long>
{
    public List<Client> findByRentalIsNull();
}