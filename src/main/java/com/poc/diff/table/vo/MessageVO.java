package com.poc.diff.table.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 10/12/2021 17:32
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
public class MessageVO {

    String ERROR_MESSAGE;
    String ERROR_CODE;
    String HOST_DATE_TIME;

}
