package me.bnnq.homework.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Apartment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;

    @ManyToOne
    @JoinColumn(name = "landlord_id")
    private Landlord landlord;

    @OneToOne(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Rental rental;
}