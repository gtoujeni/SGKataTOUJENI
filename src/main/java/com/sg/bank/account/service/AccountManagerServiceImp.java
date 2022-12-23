package com.sg.bank.account.service;

import com.sg.bank.account.util.BankConstants;
import com.sg.bank.account.exception.UnauthorizedOperationException;
import com.sg.bank.account.model.Account;
import com.sg.bank.account.model.AccountHistory;
import com.sg.bank.account.model.Transaction;
import com.sg.bank.account.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class AccountManagerServiceImp implements IAccountManagerService {

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(AccountManagerServiceImp.class));
    private final IAccountHistoryService accountHistoryService;

    public AccountManagerServiceImp(IAccountHistoryService accountHistoryService) {
        this.accountHistoryService = accountHistoryService;
    }

    /**
     *
     * @param account : the account of client
     * @return
     * This method will print the operation history
     */
    @Override
    public String showAccountHistory(Account account) {
        List<AccountHistory> accountHistories = accountHistoryService.accountHistory(account);
        StringBuilder statement = new StringBuilder();
        statement.append(BankConstants.TRANSACTION_HISTORY_HEADER).append(System.lineSeparator());

        accountHistories.stream().forEach(accountHistory ->
            statement.append(accountHistory.getTransaction().getReference()).append(BankConstants.TRANSACTION_HISTORY_SEPARATOR)
                    .append(accountHistory.getTransaction().getTransactionTimestamp()).append(BankConstants.TRANSACTION_HISTORY_SEPARATOR)
                    .append(Boolean.TRUE.equals(accountHistory.getTransaction().isDepositTransaction()) ? "" : "-")
                    .append(accountHistory.getTransaction().getAmount()).append(BankConstants.TRANSACTION_HISTORY_SEPARATOR)
                    .append(accountHistory.getBalance()).append(System.lineSeparator())
        );
        LOGGER.info(() -> "show Account History : \n" + statement.toString());
        return statement.toString();
    }

    /**
     *
     * @param account : the account of client
     * @param amount : amount deposited
     * @return transaction
     * This method will execute a deposit operation
     */
    @Override
    public Transaction depositOperation(Account account, Double amount) throws UnauthorizedOperationException{

        LOGGER.info("Deposit to account : " + account.getAccountId());
        if (amount < 0) {
            throw new UnauthorizedOperationException(account, TransactionType.DEPOSIT);
        }
        Transaction transaction = Transaction.builder().amount(amount)
                .transactionType(TransactionType.DEPOSIT)
                .transactionTimestamp(LocalDateTime.now())
                .build();

        Double sommeBalance = account.getBalanceAccount() + amount;
        account.setBalanceAccount(sommeBalance);
        account.getTransactions().add(transaction);

        return transaction;
    }

    /**
     *
     * @param account : the account of client
     * @param amount : amount withdrawn
     * @return transaction
     * @throws UnauthorizedOperationException
     * This method will execute withdrawal operation
     */
    @Override
    public Transaction withdrawalOperation(Account account, Double amount) throws UnauthorizedOperationException {

        if ((account.getBalanceAccount() < amount) || amount < 0) {
            throw new UnauthorizedOperationException(account, TransactionType.WITHDRAWAL);
        }

        Transaction transaction = Transaction.builder().amount(amount)
                .transactionType(TransactionType.WITHDRAWAL)
                .transactionTimestamp(LocalDateTime.now())
                .build();

        account.setBalanceAccount(accountHistoryService.computeAccountBalance(transaction, account.getTransactions()));
        account.getTransactions().add(transaction);

        return transaction;
    }
}
