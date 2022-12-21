package com.sg.bank.account.util;

import lombok.Data;

@Data
public class BankConstants {

    private BankConstants() {
    }

    public static final String TRANSACTION_HISTORY_HEADER = "| REFERENCE | DATE | AMOUNT | BALANCE |";
    public static final String TRANSACTION_HISTORY_SEPARATOR = "|";

    public static final String FIRST_NAME = "Ghazi";

    public static final String LAST_NAME = "Toujeni";

}
