package me.bnnq.homework.Services.DataManagers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Apartment;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApartmentManager
{
    private final IApartmentRepository apartmentRepository;
    private final IRentalRepository rentalRepository;
    private final IClientRepository clientRepository;

    public void detachRental(Apartment apartment)
    {
        var rental = apartment.getRental();
        if (rental != null)
        {
            rental.setApartment(null);

            apartment.setRental(null);
            apartmentRepository.save(apartment);

            var client = rental.getClient();
            client.setRental(null);
            clientRepository.save(client);

            rental.setClient(null);
            rentalRepository.save(rental);
            rentalRepository.delete(rental);
        }

    }

}
