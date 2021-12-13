package com.poc.diff.table.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 09/12/2021 17:16
 */
@ToString
@Getter
@Setter
public class TransactionResponseVO {

    String transactionId;
    String errorCode;
    String transactionCreditConfirmation;

    public TransactionResponseVO(String transactionId, String errorCode, String transactionCreditConfirmation) {
        this.transactionId = transactionId;
        this.errorCode = errorCode;
        this.transactionCreditConfirmation = transactionCreditConfirmation;
    }
}
