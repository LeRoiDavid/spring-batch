package com.mourchidteth.learnbatch.config;

import com.mourchidteth.learnbatch.model.Student;
import com.mourchidteth.learnbatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager plateTransactionManager;

    @Bean
    public FlatFileItemReader<Student> reader() {
        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("students.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    StudentProcessor studentProcessor() {
        return new StudentProcessor();
    }

    @Bean
    public RepositoryItemWriter<Student> writer() {
        RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setRepository(studentRepository);
        itemWriter.setMethodName("save");
        return itemWriter;
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importStudent", jobRepository)
                .start(step())
                .build();
    }

    @Bean
    public Step step(){
        return new StepBuilder("csvImport", jobRepository)
                .<Student, Student>chunk(100, plateTransactionManager)
                .reader(reader())
                .processor(studentProcessor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(100);
        return taskExecutor;
    }

    private LineMapper<Student> lineMapper() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "age");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
