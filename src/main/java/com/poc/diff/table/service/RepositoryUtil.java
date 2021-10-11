/** */
package com.poc.diff.table.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** @author ileonardo */
@Repository
public class RepositoryUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryUtil.class);

  /**
   * @param table
   * @return
   * @throws IOException
   */
  private static InputStream getDataText(String table) throws IOException {
    LOGGER.info(" getDataText table={}",table);
    return new FileInputStream(table);
  }
  /**
   * @param table
   * @return
   * @throws IOException
   */
  public static BufferedReader getBufferedReader(String table) throws IOException {
    LOGGER.info(" getBufferedReader table={}",table);
    InputStream in = getDataText(table);
    return new BufferedReader(new InputStreamReader(in, "UTF-8"));
  }

  public  static String carregarArquivo(String path) {
    LOGGER.info(" carregarArquivo path={}",path);
    String linha = "";
    BufferedReader reader = null;
    StringBuilder file = new StringBuilder();
    try {
      reader = getBufferedReader(path);
      while ((linha = reader.readLine()) != null) {
        String linhaNova = linha.replace("\\","").
                 replace("\"jsonMessage\":\"[","\"jsonMessage\":[").
                 replace("]}]\"}","]}]}");
        file.append(linhaNova);
      }
    } catch (IOException e) {
      LOGGER.error(" carregarArquivo e={}",e);
      e.printStackTrace();
    }
    return file.toString();
  }
}
