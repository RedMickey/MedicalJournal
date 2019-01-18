package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="user_id")
    private Integer id;
    @Column(name="synchronization_time")
    private Timestamp synchronizationTime;
    private String name;
    private String surname;
    private String email;
    private String password;
    @Column(name="role_id")
    private Integer roleId;

}
