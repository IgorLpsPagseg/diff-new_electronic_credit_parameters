package com.poc.diff.table.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author ileonardo
 * @since 12/05/2021 09:23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralInfo implements Serializable {

    @NonNull
    private String messageInfo;
    private String maxPhoneLength;
}
