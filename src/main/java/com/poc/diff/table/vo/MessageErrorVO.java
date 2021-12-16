package com.poc.diff.table.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 14/12/2021 11:14
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageErrorVO {
    private String code;
    private String description;
}
