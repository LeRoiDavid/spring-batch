package com.mourchidteth.learnbatch.repository;

import com.mourchidteth.learnbatch.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {}
