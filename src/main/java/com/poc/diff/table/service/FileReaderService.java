package com.poc.diff.table.service;

import com.google.gson.Gson;
import com.poc.diff.table.entity.ResultTable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** */
@Service
public class FileReaderService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileReaderService.class);

  private String basePath;


  public ResultTable getResult(final String repositoryPath, final String folderName) throws Exception {
    LOGGER.info("repositoryPath "+ repositoryPath+" folderName "+  folderName);
    setBasePath(repositoryPath,folderName);
    return getResultTableByPath();
  }

  private ResultTable getResultTableByPath() {
    try {
      List<File> files = getFileList(this.basePath);
      String fileName = files.get(0).getName();
      LOGGER.info("Arquivo encontrado " + fileName);
      String newPath = this.basePath.concat("/").concat(fileName);
      String json = RepositoryUtil.carregarArquivo(newPath);
      Gson gson = new Gson();
      ResultTable resultTable = gson.fromJson(json, ResultTable.class);
   //   return new ObjectMapper(new JsonFactory()).readValue(new File(newPath), ResultTable.class);
      return resultTable;
    } catch (Exception e) {
      LOGGER.error("Erro ao ler pasta do leitor " + this.basePath, e);
      return null;
    }
  }


  /**
   * @param repositoryPath
   * @param folderName
   */
  private void setBasePath(String repositoryPath, String folderName) {
    this.basePath = joinString(repositoryPath, "/", folderName);
  }


  /**
   * @param path
   * @return
   */
  private List<File> getFileList(final String path) throws Exception {
    File file = new File(path);

    if (!file.exists()) {
      throw new FileNotFoundException("File not found");
    }

    if (file.listFiles() == null || file.listFiles().length == 0) {
      throw new Exception("File fallback is empty");
    }
    return Arrays.stream(file.listFiles()).collect(Collectors.toList());
  }

  /**
   * @param elements
   * @return
   */
  public String joinString(String... elements) {
    return StringUtils.join(elements);
  }

}
