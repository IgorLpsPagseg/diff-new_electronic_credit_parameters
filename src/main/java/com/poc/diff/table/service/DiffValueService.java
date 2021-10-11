package com.poc.diff.table.service;

import com.poc.diff.table.entity.Branch;
import com.poc.diff.table.entity.Concessionarie;
import com.poc.diff.table.entity.JsonMessage;
import com.poc.diff.table.entity.TableInfo;
import com.poc.diff.table.entity.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ileonardo
 * @since 12/05/2021 10:57
 */
@Service
public class DiffValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiffValueService.class);



    /*
     Procurando por valores que sairam, que estão na Stable, mas nao esta na Latest
   */
    public String findValuesWhiout(JsonMessage stable, JsonMessage latest){
        LOGGER.info("Procurando por valores que sairam, que estão na Stable, mas nao esta na Latest");
        StringBuffer encontrados = new StringBuffer();
        stable.getConcessionaries().forEach(
        concessionarieStable -> findConcessionarie(
                concessionarieStable,
                latest.getConcessionaries(),
                        encontrados)
        );

        //Buscando concessionaria por Latest
//        latest.getConcessionaries().forEach(
//                concessionarieLatest -> findConcessionarie(
//                        concessionarieLatest,
//                        stable.getConcessionaries(),
//                        encontrados)
//        );

        LOGGER.info("Resultado Buscas novos valores: "+encontrados.toString());
        return encontrados.toString();
    }


    /*
       Procurando por valores novos, que estão na Latest, mas nao esta na Stable
     */
    public String findNewValues(JsonMessage stable, JsonMessage latest){
        LOGGER.info("Procurando por valores novos, que estão na Latest, mas nao estão na Stable");
        StringBuffer encontrados = new StringBuffer();

        //Buscando concessionaria por Latest
        latest.getConcessionaries().forEach(
                concessionarieLatest -> findConcessionarie(
                        concessionarieLatest,
                        stable.getConcessionaries(),
                        encontrados)
        );

        LOGGER.info("Resultado Buscas novos valores: "+encontrados.toString());
        return encontrados.toString();
    }


    private void findConcessionarie(Concessionarie concessionarieLatest,
                                    List<Concessionarie> concessionariesStable,
                                    StringBuffer encontrados){

        for(Concessionarie concessionarieStable: concessionariesStable){

            if(concessionarieLatest.getName().equals(concessionarieStable.getName())){

                for(Branch bLatest: concessionarieLatest.getBranches()){

                    concessionarieStable.getBranches().forEach(
                            branchStable -> {
                                if(bLatest.getName().equals(branchStable.getName())){
                                    novosValoresEmLatest(concessionarieLatest.getName(),
                                            bLatest.getName(),
                                            encontrados,
                                            bLatest,
                                            branchStable);
                                }
                            }
                    );

                }
            }
        }
    }

    private void novosValoresEmLatest(String concessionarieLatestName,
                              String branchLatestName,
                              StringBuffer encontrados,
                              Branch bLatest,
                              Branch bStable) {
        LOGGER.info("Buscando novos Valores Em Latest: ");
        for(TableInfo tableInfoLatest: bLatest.getTablesInfo()){
            List<Value> novosValores = new ArrayList<>();
            StringBuilder valorNaoExiste = new StringBuilder();
            for(TableInfo tableInfoStable: bStable.getTablesInfo()){
                boolean existeValor = false;
                for(Value valueLatest: tableInfoLatest.getValues()){
                    for(Value valueStable: tableInfoStable.getValues()){
                        if(valueLatest.getValue().equals(valueStable.getValue())){
                            existeValor = true;
                            break;
                        }
                    }
                    if(!existeValor){
                        novosValores.add(valueLatest);
                    }
                    existeValor = false;
                }
            }
            if(!novosValores.isEmpty()){
                valorNaoExiste.append(concessionarieLatestName);
                valorNaoExiste.append(" > ");
                valorNaoExiste.append(branchLatestName);
                valorNaoExiste.append(" { ");
                StringBuilder aux = new StringBuilder();

                for(Value v: novosValores){
                    aux.append(v.getValue()).append(",");
                }

                if( aux.length() > 0 ){
                    aux.deleteCharAt( aux.length() - 1 );
                }

                valorNaoExiste.append(aux);
                valorNaoExiste.append(" } ");
            }
            encontrados.append(valorNaoExiste);
            if(valorNaoExiste.length() > 0){
                encontrados.append(" - ");
            }
        }
    }

}


