package model;

import javax.persistence.Entity;

@Entity
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

}
