package com.ssafy.nhdream.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job autoTransferWalletToWalletJob;
    private final Job savingJob;
    private final Job loanJob;

    @Scheduled(cron = "0 20 0 * * *")
    public void runWalletBatchJob() {
        runJob(autoTransferWalletToWalletJob, "Wallet Batch Job");
    }

    @Scheduled(cron = "0 10 0 * * *")
    public void runSavingBatchJob() {
        runJob(savingJob, "Saving Batch Job");
    }

    @Scheduled(cron = "0 2 0 * * *")
    public void runLoanBatchJob() {
        runJob(loanJob, "Loan Batch Job");
    }

    private void runJob(Job job, String jobName) {
        try {
            log.info("Starting {}...", jobName);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
            log.info("{} has finished.", jobName);
        } catch (Exception e) {
            log.error("{} failed", jobName, e);
        }
    }
}