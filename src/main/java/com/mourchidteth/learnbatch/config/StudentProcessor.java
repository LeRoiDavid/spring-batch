package com.mourchidteth.learnbatch.config;

import com.mourchidteth.learnbatch.model.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student item) {
        item.setId(null);
        return item;
    }
}
