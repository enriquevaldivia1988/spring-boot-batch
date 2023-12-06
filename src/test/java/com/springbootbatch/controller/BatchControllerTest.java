package com.springbootbatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BatchController.class)
public class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private Job job;

    @Test
    public void testRunBatchJobWhenJobExecutesSuccessfullyThenReturnOkStatusAndSuccessMessage() throws Exception {
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(new JobExecution(1L));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/batch/run")
                        .param("filePath", "testFilePath")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Job executed successfully"));
    }

    @Test
    public void testRunBatchJobWhenJobExecutionFailsThenReturnInternalServerErrorAndErrorMessage() throws Exception {
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenThrow(new RuntimeException("Job execution failed"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/batch/run")
                        .param("filePath", "testFilePath")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to execute job: Job execution failed"));
    }

    @Test
    public void testRunBatchJobWhenFilePathParameterIsMissingThenReturnBadRequestAndErrorMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/batch/run")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
