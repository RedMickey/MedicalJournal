package com.example.michel.rest_api.models;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "user", schema = "medical_journal", catalog = "")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="user_id")
    private Integer id;
    @Column(name="synchronization_time")
    private Timestamp synchronizationTime;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name="role_id")
    private Integer roleId;
    @Column(name="gender_id")
    private Integer genderId;
    @Column(name="birthday_year")
    private Integer birthdayYear;

}
