package com.poc.diff.table.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 09/12/2021 16:53
 */
@ToString
@Getter
@Setter
public class TransactionResponse {

    String nsuOrigin;
    String nsuHost;
    String authorizationCode;
    String acquirerId;
    String brandName;
    String bin;
    String holder;
    String localTime;
    String localDate;
    String finalEmvParameter;
    String transactionId;
}
