package model;

import javax.persistence.Entity;

@Entity
public enum Status {

    IN_PROGRESS, NEED_INFO, SOLVED, SHELVED;

}
