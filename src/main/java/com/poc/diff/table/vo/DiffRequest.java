package com.poc.diff.table.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 11/10/2021 10:40
 */
@Getter
@Setter
@ToString
public class DiffRequest {
    String repositoryPath;
    String folderName;
}
