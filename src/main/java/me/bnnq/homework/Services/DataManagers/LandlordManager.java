package me.bnnq.homework.Services.DataManagers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Landlord;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LandlordManager
{
    private final IApartmentRepository apartmentRepository;
    private final IRentalRepository rentalRepository;
    private final IClientRepository clientRepository;

    public void detachApartments(Landlord landlord)
    {
        var apartments = landlord.getApartments();
        if (apartments != null && !apartments.isEmpty())
        {
            apartments.forEach(apartment ->
            {
                apartment.setLandlord(null);

                var rental = apartment.getRental();
                if (rental != null)
                {
                    rental.setApartment(null);

                    var client = rental.getClient();
                    client.setRental(null);
                    clientRepository.save(client);
                    rental.setClient(null);

                    apartment.setRental(null);
                    apartmentRepository.save(apartment);

                    rentalRepository.save(rental);
                    rentalRepository.delete(rental);
                }

                apartmentRepository.save(apartment);
                apartmentRepository.delete(apartment);
            });
        }

    }

}