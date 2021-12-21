package com.poc.diff.table.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TerminalParametersRequest {
    String pinPadManufacturerName;
    String pinPadModel;
}
