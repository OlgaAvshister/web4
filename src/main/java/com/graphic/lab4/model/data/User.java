package com.graphic.lab4.model.data;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "s367785_users")
public class User {
    @Id
    private String username;

    private String password;


}
