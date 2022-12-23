package com.sg.bank.account;

import com.sg.bank.account.exception.UnauthorizedOperationException;
import com.sg.bank.account.model.*;
import com.sg.bank.account.service.AccountHistoryServiceImp;
import com.sg.bank.account.service.AccountManagerServiceImp;
import com.sg.bank.account.service.IAccountHistoryService;
import com.sg.bank.account.service.IAccountManagerService;
import com.sg.bank.account.util.BankConstants;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Random;

public class AccountHistoryServiceTest {

    private static final Long CLIENT_ID = Math.abs(new Random().nextLong());
    private static final Long ACCOUNT_ID = Math.abs(new Random().nextLong());
    private Client client;
    private Account account;
    @InjectMocks
    private final IAccountHistoryService accountHistoryService = new AccountHistoryServiceImp();

    @InjectMocks
    private final IAccountManagerService accountManagerService = new AccountManagerServiceImp(accountHistoryService);

    @Before
    public void executedBeforeEach(){

        client = Client.builder().clientId(CLIENT_ID).firstName(BankConstants.FIRST_NAME).lastName(BankConstants.LAST_NAME).build();

        account = Account.builder().client(client).accountId(ACCOUNT_ID).balanceAccount(900.00).build();
    }

    @Test
    public void testAccountHistory() throws UnauthorizedOperationException {

        // When
        accountManagerService.depositOperation(account, 900.00);
        Transaction withdrawalTransaction = accountManagerService.withdrawalOperation(account, 400.00);
        accountManagerService.depositOperation(account, 400.00);
        accountManagerService.withdrawalOperation(account, 100.00);
        accountManagerService.withdrawalOperation(account, 50.00);
        accountManagerService.depositOperation(account, 450.00);
        // Then
        List<AccountHistory> accountHistories = accountHistoryService.accountHistory(account);
        Assertions.assertThat(accountHistories).isNotNull();
        Assertions.assertThat(accountHistories).isNotEmpty();
        Assertions.assertThat(accountHistories).hasSize(6);
        Assertions.assertThat(accountHistories.get(1).getBalance()).isEqualTo(500.00);
        Assertions.assertThat(accountHistories.get(1).getTransaction().getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        Assertions.assertThat(accountHistories.get(1).getTransaction().getAmount()).isEqualTo(400.00);
        Assertions.assertThat(accountHistories.get(1).getTransaction()).isEqualTo(withdrawalTransaction);
    }

    @Test
    public void testComputeAccountBalance() throws UnauthorizedOperationException {

        // When
        accountManagerService.depositOperation(account, 900.00);
        Transaction withdrawalTransaction = accountManagerService.withdrawalOperation(account, 400.00);
        accountManagerService.depositOperation(account, 400.00);
        accountManagerService.withdrawalOperation(account, 100.00);
        accountManagerService.withdrawalOperation(account, 50.00);
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
