package com.sg.bank.account;

import com.sg.bank.account.exception.UnauthorizedOperationException;
import com.sg.bank.account.model.*;
import com.sg.bank.account.service.AccountHistoryServiceImp;
import com.sg.bank.account.service.AccountManagerServiceImp;
import com.sg.bank.account.service.IAccountHistoryService;
import com.sg.bank.account.service.IAccountManagerService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.cglib.core.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AccountHistoryServiceTest {

    private static final Long CLIENT_ID = new Random().nextLong();
    private static final Long ACCOUNT_ID = new Random().nextLong();


    @InjectMocks
    private IAccountHistoryService accountHistoryService = new AccountHistoryServiceImp();

    @InjectMocks
    private IAccountManagerService accountManagerService = new AccountManagerServiceImp(accountHistoryService);

    @Test
    public void testAccountHistory() throws UnauthorizedOperationException {

        Client client = Client.builder().clientId(CLIENT_ID).firstName("Ghazi").lastName("TOUJENI").build();
        // Given
        Account
                account =
                Account.builder().client(client).accountId(ACCOUNT_ID).build();

        // When
        Transaction depositTransaction = accountManagerService.depositOperation(account, 900.00);
        Transaction withdrawalTransaction = accountManagerService.withdrawalOperation(account, 400.00);
        Transaction depositTransaction2 = accountManagerService.depositOperation(account, 400.00);
        Transaction withdrawal2Transaction = accountManagerService.withdrawalOperation(account, 100.00);
        Transaction withdrawal3Transaction = accountManagerService.withdrawalOperation(account, 50.00);
        Transaction depositTransaction3 = accountManagerService.depositOperation(account, 450.00);
        // Then
        List<AccountHistory> accountHistories = accountHistoryService.accountHistory(account);
        Assertions.assertThat(accountHistories).isNotNull();
        Assertions.assertThat(accountHistories).isNotEmpty();
        Assertions.assertThat(accountHistories.size()).isEqualTo(6);
        Assertions.assertThat(accountHistories.get(1).getBalance()).isEqualTo(500.00);
        Assertions.assertThat(accountHistories.get(1).getTransaction().getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        Assertions.assertThat(accountHistories.get(1).getTransaction().getAmount()).isEqualTo(400.00);
        Assertions.assertThat(accountHistories.get(1).getTransaction()).isEqualTo(withdrawalTransaction);
    }

    @Test
    public void testComputeAccountBalance() throws UnauthorizedOperationException {

        Client client = Client.builder().clientId(CLIENT_ID).firstName("Ghazi").lastName("TOUJENI").build();
        // Given
        Account
                account =
                Account.builder().client(client).accountId(ACCOUNT_ID).build();

        // When
        Transaction depositTransaction = accountManagerService.depositOperation(account, 900.00);
        Transaction withdrawalTransaction = accountManagerService.withdrawalOperation(account, 400.00);
        Transaction depositTransaction2 = accountManagerService.depositOperation(account, 400.00);
        Transaction withdrawal2Transaction = accountManagerService.withdrawalOperation(account, 100.00);
        Transaction withdrawal3Transaction = accountManagerService.withdrawalOperation(account, 50.00);
        Transaction depositTransaction3 = accountManagerService.depositOperation(account, 450.00);
        // Then
        Double balance = accountHistoryService.computeAccountBalance(withdrawalTransaction, account.getTransactions());
        Assertions.assertThat(balance).isNotNull();
        Assertions.assertThat(balance).isEqualTo(500);
        balance = accountHistoryService.computeAccountBalance(depositTransaction3, account.getTransactions());
        Assertions.assertThat(balance).isNotNull();
        Assertions.assertThat(balance).isEqualTo(1200.0);
    }

}
