package com.poc.diff.table.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ileonardo
 * @since 11/10/2021 10:40
 */
@Getter
@Setter
public class DiffRequest {
    String repositoryPath;
    String folderName;
}
