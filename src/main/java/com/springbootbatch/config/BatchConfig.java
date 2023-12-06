package com.springbootbatch.config;

import com.springbootbatch.entities.Person;
import com.springbootbatch.step.ExcelItemReader;
import com.springbootbatch.step.PersonCsvItemReader;
import com.springbootbatch.step.PersonItemProcessor;
import com.springbootbatch.step.PersonItemWriterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * This class represents the configuration for the Spring Batch application.
 * It defines the beans and steps required for reading data from CSV and Excel
 * files,
 * processing the data, and writing it to a database.
 */
@Configuration
public class BatchConfig {

    @Bean
    public PersonItemWriterStep personItemWriter() {
        return new PersonItemWriterStep();
    }

    @Bean
    @Lazy
    public PersonCsvItemReader personCsvItemReader() {
        return new PersonCsvItemReader();
    }

    @Bean
    public PersonItemProcessor personItemProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ExcelItemReader personExcelItemReader() {
        return new ExcelItemReader();
    }

    /**
     * Creates a TaskExecutor bean for executing tasks in a separate thread pool.
     * The TaskExecutor manages a pool of worker threads and executes submitted
     * tasks asynchronously.
     * The core pool size, maximum pool size, and queue capacity can be configured.
     *
     * @return the TaskExecutor bean
     */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(50);
        return taskExecutor;
    }

    /**
     * Creates a Step for processing data in a batch job.
     *
     * @param jobRepository      The JobRepository used for storing job metadata.
     * @param transactionManager The PlatformTransactionManager used for managing
     *                           transactions.
     * @return The created Step object.
     */
    @Bean
    public Step excelStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("excelStep", jobRepository)
                .<Person, Person>chunk(1000, transactionManager)
                .reader(personExcelItemReader())
                .processor(personItemProcessor())
                .writer(personItemWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(UncategorizedSQLException.class)
                .taskExecutor(taskExecutor())
                .build();
    }

    /**
     * Creates a Step for the Spring Batch job.
     *
     * @param jobRepository      The JobRepository used for the Step.
     * @param transactionManager The PlatformTransactionManager used for the Step.
     * @return The created Step.
     */
    @Bean
    public Step csvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csvStep", jobRepository)
                .<Person, Person>chunk(1000, transactionManager)
                .reader(personCsvItemReader())
                .processor(personItemProcessor())
                .writer(personItemWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(UncategorizedSQLException.class)
                .taskExecutor(taskExecutor())
                .build();
    }

    /**
     * Creates a job for processing Excel files.
     *
     * @param jobRepository the job repository
     * @param excelStep     the step for processing Excel files
     * @return the created job
     */
    @Bean
    public Job excelJob(JobRepository jobRepository, Step excelStep) {
        return new JobBuilder("excelJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(excelStep)
                .end()
                .build();
    }

    /**
     * Creates a job for processing CSV files.
     *
     * @param jobRepository the job repository used for storing job metadata
     * @param csvStep       the step for processing CSV files
     * @return the created job
     */
    @Bean
    public Job csvJob(JobRepository jobRepository, Step csvStep) {
        return new JobBuilder("csvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(csvStep)
                .end()
                .build();
    }
}
