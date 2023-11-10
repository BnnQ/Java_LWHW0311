package me.bnnq.homework.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Rental rental;
}