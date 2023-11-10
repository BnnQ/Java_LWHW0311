package me.bnnq.homework.Services.DataManagers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Rental;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RentalManager
{
    private final IClientRepository clientRepository;
    private final IApartmentRepository apartmentRepository;
    private final IRentalRepository rentalRepository;

    public void detachClient(Rental rental)
    {
        var client = rental.getClient();
        if (client != null)
        {
            rental.setClient(null);
            client.setRental(null);

            rentalRepository.save(rental);
            clientRepository.save(client);
        }
    }

    public void detachApartment(Rental rental)
    {
        var apartment = rental.getApartment();
        if (apartment != null)
        {
            rental.setApartment(null);
            apartment.setRental(null);

            rentalRepository.save(rental);
            apartmentRepository.save(apartment);
        }
    }

}