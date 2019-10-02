package com.murphy1.serviced.serviced.model;

import javax.persistence.*;

@MappedSuperclass
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

}
