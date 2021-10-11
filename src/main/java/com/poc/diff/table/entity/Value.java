package com.poc.diff.table.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ileonardo
 * @since 12/05/2021 09:22
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Value implements Serializable {

    private String type;
    private String validPeriod;
    private String upSell;
    private String value;
    private String bonus;
}

