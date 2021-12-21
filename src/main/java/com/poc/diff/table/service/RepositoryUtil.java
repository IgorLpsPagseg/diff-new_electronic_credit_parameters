/** */
package com.poc.diff.table.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** @author ileonardo */
@Log4j2
@Repository
public class RepositoryUtil {


  /**
   * @param table
   * @return
   * @throws IOException
   */
  private static InputStream getDataText(String table) throws IOException {
    log.info(" getDataText table={}",table);
    return new FileInputStream(table);
  }
  /**
   * @param table
   * @return
   * @throws IOException
   */
  public static BufferedReader getBufferedReader(String table) throws IOException {
    log.info(" getBufferedReader table={}",table);
    InputStream in = getDataText(table);
    return new BufferedReader(new InputStreamReader(in, "UTF-8"));
  }

  public  static String carregarArquivo(String path) {
    log.info(" carregarArquivo path={}",path);
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
      log.error(" carregarArquivo e={}",e);
      e.printStackTrace();
    }
    return file.toString();
  }
}
