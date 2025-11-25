package com.cohort5.RestBil_System_Backend.Model;


import jakarta.persistence.*;

@Entity
@Table (name = "food")
public class food {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
