package me.bnnq.homework.Services.DataManagers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Client;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientManager
{
    private final IClientRepository clientRepository;
    private final IRentalRepository rentalRepository;
    private final IApartmentRepository apartmentRepository;

    public void detachRental(Client client)
    {
        var rental = client.getRental();
        if (rental != null)
        {
            rental.setClient(null);

            var apartment = rental.getApartment();
            apartment.setRental(null);
            apartmentRepository.save(apartment);

            client.setRental(null);
            clientRepository.save(client);

            rentalRepository.save(rental);
            rentalRepository.delete(rental);
        }

    }
}
