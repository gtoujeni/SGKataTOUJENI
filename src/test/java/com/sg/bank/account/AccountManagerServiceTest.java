package com.sg.bank.account;

import com.sg.bank.account.exception.UnauthorizedOperationException;
import com.sg.bank.account.model.Account;
import com.sg.bank.account.model.Client;
import com.sg.bank.account.model.Transaction;
import com.sg.bank.account.model.TransactionType;
import com.sg.bank.account.service.AccountHistoryServiceImp;
import com.sg.bank.account.service.AccountManagerServiceImp;
import com.sg.bank.account.service.IAccountHistoryService;
import com.sg.bank.account.service.IAccountManagerService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class AccountManagerServiceTest {

    private static final Long CLIENT_ID = new Random().nextLong();
    private static final Long ACCOUNT_ID = new Random().nextLong();

    @InjectMocks
    private IAccountHistoryService accountHistoryService = new AccountHistoryServiceImp();

    @InjectMocks
    private IAccountManagerService accountManagerService = new AccountManagerServiceImp(accountHistoryService);


    @Test
    public void testDepositOperation() {

        Client client = Client.builder().clientId(CLIENT_ID).firstName("Ghazi").lastName("TOUJENI").build();
        // Given
        Account
                account =
                Account.builder().client(client).accountId(ACCOUNT_ID).build();

        // When
        Transaction transaction = accountManagerService.depositOperation(account, 500.00);
        // Then
        Transaction getTransaction = account.getTransactions().stream().findFirst().get();
        Assertions.assertThat(transaction).isNotNull();
        Assertions.assertThat(transaction.getAmount()).isEqualTo(500.00);
        Assertions.assertThat(getTransaction.getAmount()).isEqualTo(500.00);
        Assertions.assertThat(getTransaction.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        Assertions.assertThat(getTransaction.getReference()).isEqualTo(transaction.getReference());
        Assertions.assertThat(getTransaction.getTransactionTimestamp()).isEqualTo(transaction.getTransactionTimestamp());
    }

    @Test
    public void testWithdrawalOperation_withAuthorizedOperation() throws UnauthorizedOperationException {

        Client client = Client.builder().clientId(CLIENT_ID).firstName("Ghazi").lastName("TOUJENI").build();
        // Given
        Account
                account =
                Account.builder().client(client).accountId(ACCOUNT_ID).build();

        // When
        Transaction depositTransaction = accountManagerService.depositOperation(account, 500.00);
        Transaction withdrawalTransaction = accountManagerService.withdrawalOperation(account, 400.00);
        // Then
        Assertions.assertThat(account.getTransactions()).isNotNull();
        Assertions.assertThat(account.getTransactions()).isNotEmpty();
        Transaction getTransaction = account.getTransactions().stream().filter(t -> !t.isDepositTransaction()).findFirst().get();
        Assertions.assertThat(withdrawalTransaction.getAmount()).isEqualTo(400.00);
        Assertions.assertThat(getTransaction.getAmount()).isEqualTo(400.00);
        Assertions.assertThat(getTransaction.getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        Assertions.assertThat(getTransaction.getReference()).isEqualTo(withdrawalTransaction.getReference());
        Assertions.assertThat(getTransaction.getTransactionTimestamp()).isEqualTo(withdrawalTransaction.getTransactionTimestamp());
    }

    @Test
    public void testWithdrawalOperation_withUnauthorizedOperation(){

        Client client = Client.builder().clientId(CLIENT_ID).firstName("Ghazi").lastName("TOUJENI").build();
        // Given
        Account
                account =
                Account.builder().client(client).accountId(ACCOUNT_ID).build();
        // When
        Transaction depositTransaction = accountManagerService.depositOperation(account, 500.00);
        Assertions.assertThatThrownBy(() -> accountManagerService.withdrawalOperation(account, 600.00))
                .isInstanceOf(UnauthorizedOperationException.class).hasMessageContaining(ACCOUNT_ID.toString());

    }

    @Test
    public void testShowHistory() throws UnauthorizedOperationException {

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
        String accountHistory = accountManagerService.showAccountHistory(account);
        Assert.assertNotNull(accountHistory);
        List<String> histories = Arrays.stream(accountHistory.split(System.lineSeparator())).collect(Collectors.toList());
        Assert.assertTrue(!histories.isEmpty());
        Assert.assertEquals(histories.size(), 7);
    }

}
