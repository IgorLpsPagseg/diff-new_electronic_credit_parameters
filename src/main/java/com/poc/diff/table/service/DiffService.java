package com.poc.diff.table.service;

import com.poc.diff.table.entity.Branch;
import com.poc.diff.table.entity.Concessionarie;
import com.poc.diff.table.entity.GeneralInfo;
import com.poc.diff.table.entity.JsonMessage;
import com.poc.diff.table.entity.ResultTable;
import com.poc.diff.table.entity.TableInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ileonardo
 * @since 12/05/2021 10:57
 */
@Log4j2
@Service
public class DiffService {


    @Autowired
    private DiffValueService diffValueService;

    public void compareTable(ResultTable resultTable){

        JsonMessage stable = null;
        JsonMessage beta = null;
        JsonMessage latest = null;

        List<JsonMessage> jsonMessage = resultTable.getJsonMessage();
        log.info(" size "+jsonMessage.size());
        log.info(" STABLE "+jsonMessage.get(0));
        if(jsonMessage.get(0) != null){
            stable = jsonMessage.get(0);
        }

        log.info(" BETA "+jsonMessage.get(1));

        if(jsonMessage.get(1) != null){
            beta = jsonMessage.get(1);
        }

        log.info(" LATEST "+jsonMessage.get(2));

        if(jsonMessage.get(2) != null){
            latest = jsonMessage.get(2);
        }

        findConcessionaireOutsideLatest(stable, latest);
        findNewConcessionaireFromLatestInStable(stable, latest);
        findNewValuesInLatest(stable, latest);
    }

    /*
        Concessionaria que não tem mais na Stable
        JsonMessage1 = stable
        jsonMessage2 = latest
     */
    public String findConcessionaireOutsideLatest(JsonMessage stable, JsonMessage jsonMessage2){
        Boolean existe = false;
        String resultado = null;

        List<Concessionarie> concessionaireOutsideLatest = new ArrayList<>();

        log.info("Procurando Concessionaria que sairam da Latest, se todas da stable estão na latest");
        for(Concessionarie stableConcessionarie: stable.getConcessionaries()){
            for(Concessionarie concessionarie2: jsonMessage2.getConcessionaries()){
                if(stableConcessionarie.getName().equals(concessionarie2.getName())){
                    existe = true;
                    break;
                }
            }
            if(!existe){
                concessionaireOutsideLatest.add(stableConcessionarie);
            }
            existe = false;
        }

        if(!concessionaireOutsideLatest.isEmpty()){
            String aux = new String();
            for(Concessionarie outSide: concessionaireOutsideLatest){
                aux = aux+" - "+ outSide.getName();
            }
            resultado = "Operadoras na Stable não encontradas na Latest: "+aux;
        }
        log.info("resultado: "+resultado);
        return resultado;
    }

    /*
        Nova Concessionaria
        Metodo para procurar Operadoras na Latest não encontradas na Stable
        JsonMessage1 = stable
        jsonMessage2 = latest
     */
    public String findNewConcessionaireFromLatestInStable(JsonMessage stable, JsonMessage latest){
        Boolean existe = false;
        String resultado = null;
        List<Concessionarie> concessionariesNovas = new ArrayList<>();
        log.info(" Procurando nova Concessionaria, se todas da latest estão na stable");
        for(Concessionarie concessionarieLatest: latest.getConcessionaries()){
            for(Concessionarie concessionarieStable: stable.getConcessionaries()){
                if(concessionarieLatest.getName().equals(concessionarieStable.getName())){
                    existe = true;
                    break;
                }
            }
            if(!existe){
                concessionariesNovas.add(concessionarieLatest);
            }
            existe = false;
        }
        if(!concessionariesNovas.isEmpty()){
            String aux = new String();
            for(Concessionarie nova: concessionariesNovas){
                aux = aux+" - "+ nova.getName();
            }
            resultado = "Operadoras na Latest não encontradas na Stable: "+aux;
        }
        log.info("resultado:"+resultado);
        return resultado;
    }









    /*
       Procurando por valores novos, que estão na Latest, mas nao esta na Stable
     */
    public String findNewValuesInLatest(JsonMessage stable, JsonMessage latest){
        log.info("Procurando por valores novos, que estão na Latest, mas nao estão na Stable");
        return diffValueService.findNewValues(stable, latest);
    }


    /*
      Procurando por valores que saíram, que estão na Stable, mas nao esta na Latest
    */
    public String findStableValuesNotFoundInLatest(JsonMessage stable, JsonMessage latest){
        log.info("Procurando por valores que saíram, que estão na Stable, mas nao estão na Latest");
        return diffValueService.findValuesWhiout(stable, latest);
    }


    public String findDifferenceInLatest(JsonMessage stable, JsonMessage latest){
        log.info("Procurando por valores que estão na Latest, mas que estão diferentes na Stable");
        StringBuffer encontrados = new StringBuffer();
        latest.getConcessionaries().forEach(
                concessionarieLatest -> differencesEmLatest(concessionarieLatest,
                        stable.getConcessionaries(),
                        encontrados)
        );
        return encontrados.toString();
    }



    private void differencesEmLatest(Concessionarie concessionarieLatest,
                                     List<Concessionarie> concessionariesStable,
                                     StringBuffer encontrados) {

        log.info("Buscando novos Valores diferentes em campos Em Latest: ");
        for(Concessionarie concessionarieStable: concessionariesStable){
            if(concessionarieLatest.getName().equals(concessionarieStable.getName())){
                for(Branch bLatest: concessionarieLatest.getBranches()){
                    StringBuffer encontrado = new StringBuffer();

                    concessionarieStable.getBranches().forEach(

                            branchStable -> {

                                if(bLatest.getName().equals(branchStable.getName())){

                                   if(!bLatest.getCode().equals(branchStable.getCode())){
                                       encontrado.append("(code:" );
                                       encontrado.append("Latest: "+bLatest.getCode());
                                       encontrado.append("," );
                                       encontrado.append("Stable: "+branchStable.getCode());
                                       encontrado.append(")" );
                                   }
                                   if(!bLatest.getVfp().equals(branchStable.getVfp())){
                                       encontrado.append("(vfp:" );
                                       encontrado.append("Latest: "+bLatest.getVfp());
                                       encontrado.append("," );
                                       encontrado.append("Stable: "+branchStable.getVfp());
                                       encontrado.append(")" );
                                   }
                                   if(!bLatest.getSubAccountLength().equals(branchStable.getSubAccountLength())){
                                       encontrado.append("(subAccountLength:" );
                                       encontrado.append("Latest: "+bLatest.getSubAccountLength());
                                       encontrado.append("," );
                                       encontrado.append("Stable: "+branchStable.getSubAccountLength());
                                       encontrado.append(")" );
                                    }

                                    veryfiGeneralInfo(branchStable, bLatest, encontrado);
                                }
                            }
                    );


                    if(encontrado.length() > 0 ){
                        encontrados.append(concessionarieLatest.getName());
                        encontrados.append(" > ");
                        encontrados.append(bLatest.getName());
                        encontrados.append(encontrado);
                    }

                }
            }
        }

    }


    private void veryfiGeneralInfo(Branch branchStable, Branch bLatest, StringBuffer encontrado){
        log.info("Buscando Valores diferentes em campos Em Latest GeneralInfo: ");
        branchStable.getTablesInfo().forEach(

                tablesInfoStable->{
                    GeneralInfo generalInfoStable= tablesInfoStable.getGeneralInfo();

                    for(TableInfo tablesInfoLatest :bLatest.getTablesInfo())
                    {
                        GeneralInfo generalInfoLatest = tablesInfoLatest.getGeneralInfo();

                        if((generalInfoLatest.getMaxPhoneLength() !=null
                                && generalInfoStable.getMaxPhoneLength()!= null)
                                && !generalInfoLatest.getMaxPhoneLength().equals(
                                 generalInfoStable.getMaxPhoneLength()
                        )){
                            encontrado.append("(MaxPhoneLength:" );
                            encontrado.append("Latest: "+generalInfoLatest.getMaxPhoneLength());
                            encontrado.append("," );
                            encontrado.append("Stable: "+generalInfoStable.getMaxPhoneLength());
                            encontrado.append(")" );
                        }

                        if((generalInfoLatest.getMessageInfo() !=null
                                && generalInfoStable.getMessageInfo() != null)
                                &&
                                !generalInfoLatest.getMessageInfo().equals(
                                 generalInfoStable.getMessageInfo()
                        )){
                            encontrado.append("(MessageInfo:" );
                            encontrado.append("Latest: "+generalInfoLatest.getMessageInfo());
                            encontrado.append("," );
                            encontrado.append("Stable: "+generalInfoStable.getMessageInfo());
                            encontrado.append(")" );
                        }
                    }
                }
        );
    }




    /*
    Procurando por novas Branchs que não estão em Latest
  */
    public String findBranchsNotFoundInLatest(JsonMessage stable, JsonMessage latest){
        log.info("Procurando por novas Branchs que não estão mais em Latest, foram retiradas");
        StringBuffer encontrados = new StringBuffer();
        stable.getConcessionaries().forEach(
                concessionarieStable -> procurarBranchNovas( concessionarieStable,
                        latest.getConcessionaries(),
                        encontrados)
        );
        return encontrados.toString();
    }

    /*
     Procurando por novas Branchs que estão em Latest
   */
    public String findNewBranchsInLatest(JsonMessage stable, JsonMessage latest){
        log.info("Procurando pr novas Branchs que estão em Latest");
        StringBuffer encontrados = new StringBuffer();
        latest.getConcessionaries().forEach(
                concessionarieLatest -> procurarBranchNovas( concessionarieLatest,
                        stable.getConcessionaries(),
                        encontrados)
        );
        return encontrados.toString();
    }

    private void procurarBranchNovas(Concessionarie concessionarieLatest,
                                     List<Concessionarie> concessionariesStable,
                                     StringBuffer encontrados){

        log.info("Buscando novas branches em Latest");

        for(Concessionarie concessionarieStable: concessionariesStable) {

            if (concessionarieLatest.getName().equals(concessionarieStable.getName())) {
                List<Branch> branchNovas = new ArrayList<>();

                concessionarieLatest.getBranches().forEach(
                        bLatest -> veryfiNewBranchInLatest(concessionarieStable.getBranches(),
                                bLatest, branchNovas)

                );

                if(!branchNovas.isEmpty()){
                    encontrados.append(concessionarieLatest.getName()).append("{");
                    encontrados.append(branchNovas).append("}");
                }

            }

        }

    }

    private void veryfiNewBranchInLatest(List<Branch> branchStableList,
                                         Branch bLatest,
                                         List<Branch> branchNovas){
        log.info("Buscando novas branches em Latest");
        Boolean existe = false;
        for(Branch branchStable: branchStableList){
            if(bLatest.getName().equals(branchStable.getName())){
                existe = true;
                break;
            }
        }
        if(!existe){
            branchNovas.add(bLatest);
        }
    }
}


