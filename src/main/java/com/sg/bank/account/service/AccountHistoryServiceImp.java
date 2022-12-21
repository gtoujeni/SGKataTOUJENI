package com.sg.bank.account.service;

import com.sg.bank.account.model.Account;
import com.sg.bank.account.model.AccountHistory;
import com.sg.bank.account.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AccountHistoryServiceImp implements IAccountHistoryService {

    /**
     *
     * @param account : the account of client
     * @return accountHistories
     * This methode return List of account histories
     */
    public List<AccountHistory> accountHistory(Account account) {
        List<AccountHistory> accountHistories = new ArrayList<>();
        account.getTransactions().forEach(t -> {
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setTransaction(t);
            accountHistory.setBalance(computeAccountBalance(t, account.getTransactions()));
            accountHistories.add(accountHistory);
        });
        return accountHistories;
    }

    /**
     *
     * @param transaction : current transaction
     * @param transactions : list of transactions by client
     * @return
     * This method will return the balance of an account at a transaction
     */
    public Double computeAccountBalance(Transaction transaction, List<Transaction> transactions) {
        return transactions.stream()
                .filter(op ->
                        op.getReference() <= transaction.getReference()
                )
                .mapToDouble(op ->
                        op.getAmount() * (Boolean.TRUE.equals(op.isDepositTransaction()) ? 1 : -1)
                ).sum();
    }
}
