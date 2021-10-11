package com.poc.diff.table.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ileonardo
 * @since 12/05/2021 09:19
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TableInfo implements Serializable {
   private List<String> areaCodes;
   private List<Value> values;
   private GeneralInfo generalInfo;
}

