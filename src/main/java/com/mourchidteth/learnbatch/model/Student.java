package com.mourchidteth.learnbatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String firstName;
        private String lastName;
        private int age;
}