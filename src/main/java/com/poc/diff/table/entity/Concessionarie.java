package com.poc.diff.table.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author ileonardo
 * @since 12/05/2021 09:14
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ToString
public class Concessionarie implements Serializable {

    private String code;
    private String type;
    private String name;
    private List<Branch> branches;


}
