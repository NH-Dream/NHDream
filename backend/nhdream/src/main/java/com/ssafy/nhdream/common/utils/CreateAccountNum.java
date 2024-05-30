package com.ssafy.nhdream.common.utils;

import com.ssafy.nhdream.domain.frdeposit.repository.FrDepositAccountRepository;
import com.ssafy.nhdream.domain.loan.repository.LoanAccountRepository;
import com.ssafy.nhdream.domain.redeposit.repository.ReDepositAccountRepository;
import com.ssafy.nhdream.domain.saving.repository.SavingAccountRepository;
import com.ssafy.nhdream.entity.frdeposit.FrDepositAccount;
import com.ssafy.nhdream.entity.loan.LoanAccount;
import com.ssafy.nhdream.entity.redeposit.ReDepositAccount;
import com.ssafy.nhdream.entity.saving.SavingAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 사용 방법 : CreateAccountNum 빈 주입 후 .getUsableAccountNum 함수 호출
 * @Param :  1 - 대출  2 - 예금  3 - 적금  4 - 자유 입출금
 */
@Component
@RequiredArgsConstructor
public class CreateAccountNum {
    private final LoanAccountRepository loanAccountRepository;
    private final ReDepositAccountRepository redepositAccountRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final FrDepositAccountRepository frDepositAccountRepository;
    public String getUsableAccountNum(int accountType) {

        // "209" + "계좌 타입" + "계좌 타입" + "00001 부터 99999 사이의 숫자"
        String newAccountNum = "";

        // 계좌 뒤에서 5자리 사용중인지 체크하는 배열
        boolean[] usedAccountMap = new boolean[100000];

        // 1 : 대출  2 : 예금  3 : 적금 4 : 자유입출금(지갑)
        if (accountType == 1) {

            // 현재 사용 되고 있는 계좌번호 리스트 받아오기
            List<LoanAccount> currentList = loanAccountRepository.findAll();

            // 계좌번호 계좌 타입 다음으로 들어갈 숫자 ex) 00001
            int restAccountNum = 1;

            // 사용중인 계좌들을 전부 순회하면서 사용 중이라는 표기 해주기
            for (int i = 0 ; i < currentList.size(); i++) {
                // 계좌번호 받아와서
                String acn = currentList.get(i).getAccountNum();

                // 뒤에서 5자리만 끊은 값을
                acn = acn.substring(acn.length()-5, acn.length());

                // 배열에 사용중 표기하기
                usedAccountMap[Integer.parseInt(acn)] = true;
            }

            // 1부터 시작해서 사용할 수 있는 값이 나올 때 까지 더해주기
            while(usedAccountMap[restAccountNum]) {
                restAccountNum++;
            }

            // 결과 포맷팅 해주기
            newAccountNum = "20911" + String.format("%05d", restAccountNum);

        } else if (accountType == 2) {
            // 현재 사용 되고 있는 계좌번호 리스트 받아오기
            List<ReDepositAccount> currentList = redepositAccountRepository.findAll();

            // 계좌번호 계좌 타입 다음으로 들어갈 숫자 ex) 00001
            int restAccountNum = 1;

            // 사용중인 계좌들을 전부 순회하면서 사용 중이라는 표기 해주기
            for (int i = 0 ; i < currentList.size(); i++) {
                // 계좌번호 받아와서
                String acn = currentList.get(i).getAccountNum();

                // 뒤에서 5자리만 끊은 값을
                acn = acn.substring(acn.length()-5, acn.length());

                // 배열에 사용중 표기하기
                usedAccountMap[Integer.parseInt(acn)] = true;
            }

            // 1부터 시작해서 사용할 수 있는 값이 나올 때 까지 더해주기
            while(usedAccountMap[restAccountNum]) {
                restAccountNum++;
            }

            // 결과 포맷팅 해주기
            newAccountNum = "20922" + String.format("%05d", restAccountNum);

        } else if (accountType == 3) {

            // 현재 사용 되고 있는 계좌번호 리스트 받아오기
            List<SavingAccount> currentList = savingAccountRepository.findAll();

            // 계좌번호 계좌 타입 다음으로 들어갈 숫자 ex) 00001
            int restAccountNum = 1;

            // 사용중인 계좌들을 전부 순회하면서 사용 중이라는 표기 해주기
            for (int i = 0 ; i < currentList.size(); i++) {
                // 계좌번호 받아와서
                String acn = currentList.get(i).getAccountNum();

                // 뒤에서 5자리만 끊은 값을
                acn = acn.substring(acn.length()-5, acn.length());

                // 배열에 사용중 표기하기
                usedAccountMap[Integer.parseInt(acn)] = true;
            }

            // 1부터 시작해서 사용할 수 있는 값이 나올 때 까지 더해주기
            while(usedAccountMap[restAccountNum]) {
                restAccountNum++;
            }

            // 결과 포맷팅 해주기
            newAccountNum = "20933" + String.format("%05d", restAccountNum);
        } else if (accountType == 4) {

            // 현재 사용 되고 있는 계좌번호 리스트 받아오기
            List<FrDepositAccount> currentList = frDepositAccountRepository.findAll();

            // 계좌번호 계좌 타입 다음으로 들어갈 숫자 ex) 00001
            int restAccountNum = 1;

            // 사용중인 계좌들을 전부 순회하면서 사용 중이라는 표기 해주기
            for (int i = 0 ; i < currentList.size(); i++) {
                // 계좌번호 받아와서
                String acn = currentList.get(i).getAccountNum();

                // 뒤에서 5자리만 끊은 값을
                acn = acn.substring(acn.length()-5, acn.length());

                // 배열에 사용중 표기하기
                usedAccountMap[Integer.parseInt(acn)] = true;
            }

            // 1부터 시작해서 사용할 수 있는 값이 나올 때 까지 더해주기
            while(usedAccountMap[restAccountNum]) {
                restAccountNum++;
            }

            // 결과 포맷팅 해주기
            newAccountNum = "20944" + String.format("%05d", restAccountNum);
        } else {
            // 잘못 된 인자
            return "잘못 된 인자";
        }

        return newAccountNum;
    }
}
