package com.poc.diff.table.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 09/12/2021 10:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class AuthenticationRequest {

    private String          applicationCode;
    private String             readerModel ;
    private String            serialNumber ;
    private String           activationCode;
    private Integer           qtd;
    private EncodeSaleRequest encodeSale;
    private ClubePagConsumerIdRequest clubePagConsumerId;
    private TerminalParametersRequest terminalParametersRequest;
}
