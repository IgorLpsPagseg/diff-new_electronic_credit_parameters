package com.poc.diff.table.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 09/12/2021 17:16
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseVO {

    String transactionId;
    String errorCode;
    String transactionCreditConfirmation;
    String description;

    public TransactionResponseVO(String transactionId, String errorCode, String transactionCreditConfirmation, String description) {
        this.transactionId = transactionId;
        this.errorCode = errorCode;
        this.transactionCreditConfirmation = transactionCreditConfirmation;
        this.description = description;
    }
}
