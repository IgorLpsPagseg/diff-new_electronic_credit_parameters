package com.poc.diff.table.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 10/12/2021 10:05
 */
@ToString
@Getter
@Setter
public class ClubePagRedeemableOfferListRequest {

    private Integer qtd;
    private String          applicationCode;
    private String             readerModel ;
    private String            serialNumber ;
    private String           activationCode;
    private String phoneNumber;
    private String totalAmount;

}
