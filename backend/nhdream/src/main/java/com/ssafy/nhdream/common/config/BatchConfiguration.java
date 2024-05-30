package com.ssafy.nhdream.common.config;

import com.ssafy.nhdream.common.exception.CustomException;
import com.ssafy.nhdream.common.exception.ExceptionType;
import com.ssafy.nhdream.common.utils.CalculateDate;
import com.ssafy.nhdream.common.utils.TransferService;
import com.ssafy.nhdream.domain.frdeposit.repository.AutomaticTransferTaskRepository;
import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.loan.repository.LoanAccountRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingAccountRepository;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.loan.LoanAccount;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import com.ssafy.nhdream.entity.transfer.AutomaticTransferTask;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
//@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FrDepositAccountRepository frDepositAccountRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final LoanAccountRepository loanAccountRepository;
    private final TransferService transferService;

    public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager, FrDepositAccountRepository frDepositAccountRepository, SavingAccountRepository savingAccountRepository, LoanAccountRepository loanAccountRepository, TransferService transferService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.frDepositAccountRepository = frDepositAccountRepository;
        this.savingAccountRepository = savingAccountRepository;
        this.loanAccountRepository = loanAccountRepository;
        this.transferService = transferService;
    }



    //지갑에서 지갑으로 가는 자동이체 배치
    @Bean
    public Job autoTransferWalletToWalletJob() {
        return new JobBuilder("autoTransferWalletToWalletJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(walletStep1())
                .build();
    }

    @Bean
    public Step walletStep1() {
        return new StepBuilder("walletStep1", jobRepository)
                .<AutomaticTransferTask, AutomaticTransferTask>chunk(10, transactionManager)
                .reader(walletReader(null,null,null))
                .processor(walletProcessor())
                .writer(walletWriter(null))
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<AutomaticTransferTask> walletReader(EntityManagerFactory entityManagerFactory,
                                                                   @Value("#{jobParameters['yesterday']}") LocalDate yesterday,
                                                                   @Value("#{jobParameters['today']}") LocalDate today) {

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("yesterday", LocalDate.now().minusDays(1));
        parameterValues.put("today", LocalDate.now());
        return new JpaPagingItemReaderBuilder<AutomaticTransferTask>()
                .name("walletReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT att FROM AutomaticTransferTask att WHERE att.nextScheduleTime BETWEEN :yesterday and :today AND att.isActive = true AND att.type = 0")
                .parameterValues(parameterValues)
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor<AutomaticTransferTask, AutomaticTransferTask> walletProcessor() {
        return automaticTransferToFrDepositTask -> {
            LocalDate today = LocalDate.now();

            log.info("{}", automaticTransferToFrDepositTask.getId());

            FrDepositAccount senderAccount = automaticTransferToFrDepositTask.getSenderWalletAccount();
            //받는사람계좌
            FrDepositAccount recipientAccount = frDepositAccountRepository.findFrDepositAccountByContractAddress(automaticTransferToFrDepositTask.getRecipientAccount())
                    .orElseThrow(() -> new CustomException(ExceptionType.FRACCOUNT_NOT_EXIST));
            //계좌이체 진행
            transferService.transferFromWalletToWallet(senderAccount, recipientAccount, automaticTransferToFrDepositTask.getRecurringAmount());

            //계좌이체 했으면 계좌이체 항목 업데이트
            LocalDate nextScheduleTime = CalculateDate.nextValidDate(today.plusMonths(1), automaticTransferToFrDepositTask.getTransferDay());

            automaticTransferToFrDepositTask.updateNextScheduleTime(nextScheduleTime);

            //자동이체 다 끝났으면
            if (nextScheduleTime.isAfter(automaticTransferToFrDepositTask.getExpiredAt())) {
                automaticTransferToFrDepositTask.updatedIsActive();
            }


            log.info("자동이체Id: {},userId: {} 성공",automaticTransferToFrDepositTask.getId(),automaticTransferToFrDepositTask.getUser().getId());
            return automaticTransferToFrDepositTask;
        };
    }

    @Bean
    public ItemWriter<AutomaticTransferTask> walletWriter(AutomaticTransferTaskRepository automaticTransferTaskRepository) {
        return automaticTransferTaskRepository::saveAll;
    }

    //적금 배치
    //지갑->적금, 이자, 만기 순으로 스텝예정
    @Bean
    public Job savingJob() {
        return new JobBuilder("savingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(savingStep1())
                .build();
    }

    @Bean
    public Step savingStep1() {
        return new StepBuilder("savingStep1", jobRepository)
                .<AutomaticTransferTask, AutomaticTransferTask>chunk(10, transactionManager)
                .reader(savingReader1(null,null,null))
                .processor(savingProcessor1())
                .writer(savingWriter1(null))
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<AutomaticTransferTask> savingReader1(EntityManagerFactory entityManagerFactory,
                                                                    @Value("#{jobParameters['yesterday']}") LocalDate yesterday,
                                                                    @Value("#{jobParameters['today']}") LocalDate today) {

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("yesterday", LocalDate.now().minusDays(1));
        parameterValues.put("today", LocalDate.now());
        return new JpaPagingItemReaderBuilder<AutomaticTransferTask>()
                .name("savingReader1")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT att FROM AutomaticTransferTask att WHERE att.nextScheduleTime BETWEEN :yesterday and :today AND att.isActive = true AND att.type = 1")
                .parameterValues(parameterValues)
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor<AutomaticTransferTask, AutomaticTransferTask> savingProcessor1() {
        return automaticTransferToSavingTask -> {
            LocalDate today = LocalDate.now();

            log.info("{}", automaticTransferToSavingTask.getId());

            LocalDate nextScheduleTime = CalculateDate.nextValidDate(today.plusMonths(1), automaticTransferToSavingTask.getTransferDay());

            //보내는 지갑 찾기
            FrDepositAccount senderAccount = automaticTransferToSavingTask.getSenderWalletAccount();

            //돈 넣을 적금계좌 찾기
            SavingAccount recipientAccount = savingAccountRepository.findByAccountNum(automaticTransferToSavingTask.getRecipientAccount())
                    .orElseThrow(() -> new CustomException(ExceptionType.SAVINGACCOUNT_NOT_EXIST));

            //계좌이체 진행
            transferService.transferFromWalletToSaving(senderAccount, recipientAccount, automaticTransferToSavingTask.getRecurringAmount(), nextScheduleTime);


            //계좌이체 했으니 항목업데이트
            log.info("다음일정{}", nextScheduleTime);
            automaticTransferToSavingTask.updateNextScheduleTime(nextScheduleTime);


            //만기됐으면 자동이체 끝
            if (!recipientAccount.isActive()) {
                automaticTransferToSavingTask.updatedIsActive();
            }

            log.info("userId: {}, 자동이체Id: {} 성공", automaticTransferToSavingTask.getUser().getId(), automaticTransferToSavingTask.getId());
            return automaticTransferToSavingTask;
        };
    }

    @Bean
    public ItemWriter<AutomaticTransferTask> savingWriter1(AutomaticTransferTaskRepository automaticTransferTaskRepository) {
        return automaticTransferTaskRepository::saveAll;
    }



    //대출 배치
    //지갑->대출, 이자, 만기 순으로 스텝예정
    @Bean
    public Job loanJob() {
        return new JobBuilder("loanJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(loanStep1())
                .build();
    }

    @Bean
    public Step loanStep1() {
        return new StepBuilder("loanStep1", jobRepository)
                .<AutomaticTransferTask, AutomaticTransferTask>chunk(10, transactionManager)
                .reader(loanReader1(null,null,null))
                .processor(loanProcessor1())
                .writer(loanWriter1(null))
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<AutomaticTransferTask> loanReader1(EntityManagerFactory entityManagerFactory,
                                                                   @Value("#{jobParameters['yesterday']}") LocalDate yesterday,
                                                                   @Value("#{jobParameters['today']}") LocalDate today) {

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("yesterday", LocalDate.now().minusDays(1));
        parameterValues.put("today", LocalDate.now());
        return new JpaPagingItemReaderBuilder<AutomaticTransferTask>()
                .name("loanReader1")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT att FROM AutomaticTransferTask att WHERE att.nextScheduleTime BETWEEN :yesterday and :today AND att.isActive = true AND att.type = 2")
                .parameterValues(parameterValues)
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor<AutomaticTransferTask, AutomaticTransferTask> loanProcessor1() {
        return automaticTransferToLoanTask -> {
            LocalDate today = LocalDate.now();

            log.info("{}", automaticTransferToLoanTask.getId());

            LocalDate nextScheduleTime = CalculateDate.nextValidDate(today.plusMonths(1), automaticTransferToLoanTask.getTransferDay());

            //보내는 지갑 찾기
            FrDepositAccount senderAccount = automaticTransferToLoanTask.getSenderWalletAccount();

            //돈 넣을 대출계좌 찾기
            LoanAccount loanAccount = loanAccountRepository.findByAccountNum(automaticTransferToLoanTask.getRecipientAccount())
                    .orElseThrow(() -> new CustomException(ExceptionType.SAVINGACCOUNT_NOT_EXIST));

            //계좌이체 진행 위에 함수 정의되어있음
            transferService.transferFromWalletToLoan(senderAccount, loanAccount, automaticTransferToLoanTask.getRecurringAmount(), nextScheduleTime);


            //계좌이체 했으니 항목업데이트
            automaticTransferToLoanTask.updateNextScheduleTime(nextScheduleTime);

            //만기됐으면 자동이체 끝
            if (loanAccount.getState() == 1) {
                automaticTransferToLoanTask.updatedIsActive();
            }

            log.info("userId: {}, 자동이체Id: {} 성공", automaticTransferToLoanTask.getUser().getId(), automaticTransferToLoanTask.getId());



            log.info("userId: {}, 자동이체Id: {} 성공", automaticTransferToLoanTask.getUser().getId(), automaticTransferToLoanTask.getId());
            return automaticTransferToLoanTask;
        };
    }

    @Bean
    public ItemWriter<AutomaticTransferTask> loanWriter1(AutomaticTransferTaskRepository automaticTransferTaskRepository) {
        return automaticTransferTaskRepository::saveAll;
    }


}


// 스틱스강
//    private final AutomaticTransferTaskRepository automaticTransferTaskRepository;
//
//    @Bean(name = "simpleJob1")
//    public Job simpleJob1(JobRepository jobRepository, Step simpleStep1) {
//        log.info(">>> simpleJob1");
//        return new JobBuilder("simpleJob1", jobRepository)
//                .start(simpleStep1)
//                .build();
//    }
//
//    @Bean("simpleStep1")
//    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
//        log.info(">>> simpleStep1");
//        return new StepBuilder("simpleStep1", jobRepository)
//                .tasklet(testTasklet, platformTransactionManager).build();
//    }
//
//    @Bean
//    public Tasklet testTasklet(){
//        return ((contribution, chunkContext) -> {
//            log.info(">>>>> Tasklet");
//
//            automaticTransferTaskRepository.getAutomaticTransferTaskLoan();
//
//            return RepeatStatus.FINISHED;
//        });
//    }
//
//    @Bean
//    public Step sampleStep(JobRepository jobRepository,
//                           PlatformTransactionManager transactionManager) {
//        return new StepBuilder("sampleStep", jobRepository)
//                .<String, String>chunk(10, transactionManager)
//                .reader(itemReader())
//                .writer(itemWriter())
//                .build();
//    }
