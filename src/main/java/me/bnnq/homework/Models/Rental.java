package me.bnnq.homework.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;

import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    private Date rentStartDate;
    private Date rentEndDate;
}