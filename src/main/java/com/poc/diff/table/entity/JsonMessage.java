package com.poc.diff.table.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ileonardo
 * @since 12/05/2021 09:13
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JsonMessage implements Serializable {
    String vgp;
    List<Concessionarie> concessionaries;

}
