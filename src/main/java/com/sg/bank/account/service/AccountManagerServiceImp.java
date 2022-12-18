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

    private final static Logger logger = Logger.getLogger(String.valueOf(AccountManagerServiceImp.class));
    private final IAccountHistoryService accountHistoryService;

    public AccountManagerServiceImp(IAccountHistoryService accountHistoryService) {
        this.accountHistoryService = accountHistoryService;
    }

    /**
     *
     * @param account
     * @return
     * This method will print the operation history
     */
    @Override
    public String showAccountHistory(Account account) {
        List<AccountHistory> accountHistories = accountHistoryService.accountHistory(account);
        StringBuilder statement = new StringBuilder();
        statement.append(BankConstants.TRANSACTION_HISTORY_HEADER).append(System.lineSeparator());

        accountHistories.stream().forEach(accountHistory -> {
            statement.append(accountHistory.getTransaction().getReference()).append(BankConstants.TRANSACTION_HISTORY_SEPARATOR)
                    .append(accountHistory.getTransaction().getTransactionTimestamp()).append(BankConstants.TRANSACTION_HISTORY_SEPARATOR)
                    .append(accountHistory.getTransaction().isDepositTransaction() ? "" : "-")
                    .append(accountHistory.getTransaction().getAmount()).append(BankConstants.TRANSACTION_HISTORY_SEPARATOR)
                    .append(accountHistory.getBalance()).append(System.lineSeparator());
        });
        logger.info(statement.toString());
        return statement.toString();
    }

    /**
     *
     * @param account
     * @param amount
     * @return transaction
     * This method will execute a deposit operation
     */
    @Override
    public Transaction depositOperation(Account account, Double amount) {

        logger.info("Deposit to account : " + account.getAccountId());
        Transaction transaction = Transaction.builder().amount(amount).build();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionTimestamp(LocalDateTime.now());
        account.getTransactions().add(transaction);

        return transaction;
    }

    /**
     *
     * @param account
     * @param amount
     * @return transaction
     * @throws UnauthorizedOperationException
     * This method will execute withdrawal operation
     */
    @Override
    public Transaction withdrawalOperation(Account account, Double amount) throws UnauthorizedOperationException {

        Transaction transaction = Transaction.builder().amount(amount).build();
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setTransactionTimestamp(LocalDateTime.now());
        if (accountHistoryService.computeAccountBalance(transaction, account.getTransactions()) < amount) {
            throw new UnauthorizedOperationException(account, TransactionType.WITHDRAWAL);
        }
        account.getTransactions().add(transaction);

        return transaction;
    }
}
