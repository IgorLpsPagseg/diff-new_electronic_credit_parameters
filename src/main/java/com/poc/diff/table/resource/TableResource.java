package com.poc.diff.table.resource;

import com.poc.diff.table.entity.JsonMessage;
import com.poc.diff.table.entity.ResultTable;
import com.poc.diff.table.service.DiffService;
import com.poc.diff.table.service.FileReaderService;
import com.poc.diff.table.vo.DiffReporVO;
import com.poc.diff.table.vo.DiffRequest;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author ileonardo
 * @since 12/05/2021 08:57
 */
@Log4j2
@RestController
@RequestMapping("/switch_new_electronic_credit_parameters")
public class TableResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableResource.class);

    @Autowired
    private FileReaderService fileReaderService;

    @Autowired
    private DiffService diffService;


    @RequestMapping(
            value = {"/result"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTable> resultTable(@RequestParam(value = "folderName" ) String folderName) throws Exception {
        String repositoryPath = "/home/ileonardo/Documents/Sustentaçao/Recarga/2021";
     //   String folderName = "SDPE-476789";
        ResultTable resultTable = fileReaderService.getResult(repositoryPath, folderName);

        diffService.compareTable(resultTable);

        return ResponseEntity
                .ok()
                .body(resultTable);
    }


    @RequestMapping(
            value = {"/result-report"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffReporVO> resultReport(@RequestParam(value = "folderName" ) String folderName) throws Exception {
        String repositoryPath = "/home/ileonardo/Documents/Sustentaçao/Recarga/2021";

        ResultTable resultTable = fileReaderService.getResult(repositoryPath, folderName);

        JsonMessage stable = null;
        JsonMessage beta = null;
        JsonMessage latest = null;

        List<JsonMessage> jsonMessage = resultTable.getJsonMessage();
        LOGGER.info(" size "+jsonMessage.size());
        LOGGER.info(" STABLE "+jsonMessage.get(0));
        if(jsonMessage.get(0) != null){
            stable = jsonMessage.get(0);
        }

        LOGGER.info(" BETA "+jsonMessage.get(1));

        if(jsonMessage.get(1) != null){
            beta = jsonMessage.get(1);
        }

        LOGGER.info(" LATEST "+jsonMessage.get(2));

        if(jsonMessage.get(2) != null){
            latest = jsonMessage.get(2);
        }


        DiffReporVO vo = new DiffReporVO(
                diffService.findNewValuesInLatest(stable, latest),
                diffService.findStableValuesNotFoundInLatest(stable, latest),
                diffService.findNewConcessionaireFromLatestInStable(stable, latest),
                diffService.findConcessionaireOutsideLatest(stable, latest),
                diffService.findNewBranchsInLatest(stable, latest),
                diffService.findBranchsNotFoundInLatest(stable, latest),
                diffService.findDifferenceInLatest(stable, latest));

        return ResponseEntity
                .ok()
                .body(vo);
    }


    @RequestMapping(
            value = {"/diff"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffReporVO> resultReport(@RequestBody DiffRequest request) throws Exception {
        String repositoryPath = request.getRepositoryPath();
        String folderName = request.getFolderName();
        System.out.println( " path: "+repositoryPath+ " folderName: "+folderName);

        ResultTable resultTable = fileReaderService.getResult(repositoryPath, folderName);

        JsonMessage stable = null;
        JsonMessage beta = null;
        JsonMessage latest = null;

        List<JsonMessage> jsonMessage = resultTable.getJsonMessage();
        LOGGER.info(" size "+jsonMessage.size());
        LOGGER.info(" STABLE "+jsonMessage.get(0));
        if(jsonMessage.get(0) != null){
            stable = jsonMessage.get(0);
        }

        LOGGER.info(" BETA "+jsonMessage.get(1));

        if(jsonMessage.get(1) != null){
            beta = jsonMessage.get(1);
        }

        LOGGER.info(" LATEST "+jsonMessage.get(2));

        if(jsonMessage.get(2) != null){
            latest = jsonMessage.get(2);
        }


        DiffReporVO vo = new DiffReporVO(
                diffService.findNewValuesInLatest(stable, latest),
                diffService.findStableValuesNotFoundInLatest(stable, latest),
                diffService.findNewConcessionaireFromLatestInStable(stable, latest),
                diffService.findConcessionaireOutsideLatest(stable, latest),
                diffService.findNewBranchsInLatest(stable, latest),
                diffService.findBranchsNotFoundInLatest(stable, latest),
                diffService.findDifferenceInLatest(stable, latest));

        return ResponseEntity
                .ok()
                .body(vo);
    }



    @RequestMapping(
            value = {"/result-report/getall"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTable> getall(@RequestParam(value = "folderName" ) String folderName) throws Exception {
        String repositoryPath = "/home/ileonardo/Documents/Sustentaçao/Recarga/2021";
        return ResponseEntity
                .ok()
                .body(fileReaderService.getResult(repositoryPath, folderName));
    }


}



