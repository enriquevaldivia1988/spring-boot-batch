package com.springbootbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootbatch.step.ExcelItemReader;
import com.springbootbatch.step.PersonCsvItemReader;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final JobLauncher jobLauncher;

    @Autowired
    @Qualifier("excelJob")
    private Job excelJob;

    @Autowired
    @Qualifier("csvJob")
    private Job csvJob;
    private final ExcelItemReader excelItemReader;

    private final PersonCsvItemReader csvItemReader;

    @Autowired
    public BatchController(JobLauncher jobLauncher, ExcelItemReader excelItemReader,
            PersonCsvItemReader csvItemReader) {
        this.jobLauncher = jobLauncher;
        this.excelItemReader = excelItemReader;
        this.csvItemReader = csvItemReader;
    }

    /**
     * Runs a batch job based on the provided file path and job type.
     * 
     * @param filePath The path of the file to be processed.
     * @param jobType  The type of the job to be executed (excel or csv).
     * @return A Mono containing the ResponseEntity with the result of the job
     *         execution.
     */
    @PostMapping("/run")
    public Mono<ResponseEntity<String>> runBatchJob(@RequestParam("filePath") String filePath,
            @RequestParam("jobType") String jobType) {
        return Mono.fromCallable(() -> {
            // Create job parameters
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            if ("excel".equalsIgnoreCase(jobType)) {
                // Set the file path for the Excel reader
                excelItemReader.setFilename(filePath);

                // Run the Excel job
                jobLauncher.run(excelJob, jobParameters);
            } else if ("csv".equalsIgnoreCase(jobType)) {
                // Set the file path for the CSV reader
                csvItemReader.setFilename(filePath);

                // Run the CSV job
                jobLauncher.run(csvJob, jobParameters);
            } else {
                return ResponseEntity.badRequest().body("Invalid job type: " + jobType);
            }

            return ResponseEntity.ok("Job executed successfully");
        }).onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to execute job: " + e.getMessage())));
    }
}
