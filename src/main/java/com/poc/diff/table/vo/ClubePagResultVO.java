package com.poc.diff.table.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

import java.util.List;

/**
 * @author ileonardo
 * @since 10/12/2021 16:46
 */
@ToString
@Getter
@AllArgsConstructor
public class ClubePagResultVO {
    List<MessageVO> transactions;

    public ClubePagResultVO(){}


    public void setTransactions(List<MessageVO> transactions) {
        this.transactions = transactions;
    }
}
