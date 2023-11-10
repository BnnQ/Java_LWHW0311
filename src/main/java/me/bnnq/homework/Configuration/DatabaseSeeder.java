package me.bnnq.homework.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import me.bnnq.homework.Models.Apartment;
import me.bnnq.homework.Models.Client;
import me.bnnq.homework.Models.Landlord;
import me.bnnq.homework.Models.Rental;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.ILandlordRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.util.ArrayList;

@Configuration
public class DatabaseSeeder {
    @Bean
    @Transactional
    CommandLineRunner initDatabase(IClientRepository clientRepository, ILandlordRepository landlordRepository, IApartmentRepository apartmentRepository, IRentalRepository rentalRepository) {
        return args -> {
            if (clientRepository.count() == 0 && landlordRepository.count() == 0 && apartmentRepository.count() == 0 && rentalRepository.count() == 0) {
                var landlord1 = new Landlord(null, "John", "Doe", "john.doe@example.com", new ArrayList<>());
                var landlord2 = new Landlord(null, "Jane", "Doe", "jane.doe@example.com", new ArrayList<>());

                landlord1 = landlordRepository.save(landlord1);
                landlord2 = landlordRepository.save(landlord2);

                var apartment1 = new Apartment(null, "123 Main St", landlord1, null);
                var apartment2 = new Apartment(null, "456 Elm St", landlord1, null);
                var apartment3 = new Apartment(null, "789 Oak St", landlord2, null);

                apartment1 = apartmentRepository.save(apartment1);
                apartment2 = apartmentRepository.save(apartment2);
                apartment3 = apartmentRepository.save(apartment3);

                var client1 = new Client(null, "Alice", "Smith", "alice.smith@example.com", null);
                var client2 = new Client(null, "Bob", "Johnson", "bob.johnson@example.com", null);

                client1 = clientRepository.save(client1);
                client2 = clientRepository.save(client2);

                var rental1 = new Rental(null, client1, apartment1, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
                var rental2 = new Rental(null, client2, apartment2, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));

                rental1 = rentalRepository.save(rental1);
                rental2 = rentalRepository.save(rental2);

                client1.setRental(rental1);
                client2.setRental(rental2);

                clientRepository.save(client1);
                clientRepository.save(client2);
            }
        };
    }
}