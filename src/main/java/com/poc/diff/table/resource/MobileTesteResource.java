package com.poc.diff.table.resource;

import com.poc.diff.table.service.MobileClubePagTesteService;
import com.poc.diff.table.service.MobileService;
import com.poc.diff.table.vo.AuthenticationRequest;
import com.poc.diff.table.vo.ClubePagConsumerIdRequest;
import com.poc.diff.table.vo.ClubePagRedeemableOfferListRequest;
import com.poc.diff.table.vo.ClubePagResultVO;
import com.poc.diff.table.vo.EncodeSaleRequest;
import com.poc.diff.table.vo.MessageVO;
import com.poc.diff.table.vo.TransactionResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author ileonardo
 * @since 16/11/2021 17:26
 */
@RestController
@RequestMapping(path = "mobile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class MobileTesteResource {

private static final Logger LOGGER = LoggerFactory.getLogger(MobileTesteResource.class);

@Autowired
private MobileService service;

@Autowired
private MobileClubePagTesteService mobileClubePagTesteService;


    @PostMapping(value = "/transaction")
    public ResponseEntity<TransactionResponseVO> transaction(@RequestBody AuthenticationRequest request) throws Exception {
        LOGGER.info("TRANSACTION ________________");
        String token = service.authenticationRequest(request);
        return new ResponseEntity<>(service.createTransaction(request, token) , HttpStatus.OK);
    }



    @PostMapping(value = "/transaction-load")
    public ResponseEntity<List<String>> transactionLoad(@RequestBody AuthenticationRequest request) throws Exception {
        LOGGER.info("TRANSACTION LOAD________________");
        List<String> transactions =  new ArrayList<>();
        String token = service.authenticationRequest(request);
        for(int i =0; i < request.getQtd(); i++){
            //TransactionResponseVO transaction = service.createTransaction(request, token);
            Future<TransactionResponseVO> futureVO =  tansactionResponseVOAsync(request, token);
            TransactionResponseVO transaction = futureVO.get();

            if(transaction.getTransactionId() != null){
                transactions.add(transaction.getTransactionId()+"; "+transaction.getErrorCode());
            }
            Integer value = Integer.valueOf(request.getEncodeSale().getAmmount());
            Integer newValue = value + 10;
            request.getEncodeSale().setAmmount(preencheCharEsquerda(String.valueOf(newValue), 10, "0"));
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }




    @PostMapping(value = "/clubePagRedeemableOfferList")
    public ResponseEntity<List<MessageVO>> clubePagRedeemableOfferList(@RequestBody ClubePagRedeemableOfferListRequest request) throws Exception {
        LOGGER.info("clubePagRedeemableOfferList LOAD________________");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setActivationCode(request.getActivationCode());
        authenticationRequest.setApplicationCode(request.getApplicationCode());
        authenticationRequest.setReaderModel(request.getReaderModel());
        authenticationRequest.setSerialNumber(request.getSerialNumber());


        String token = service.authenticationRequest(authenticationRequest);


        List<MessageVO> transactions =  new ArrayList<>();

        for(int i =0; i < request.getQtd(); i++){

            Integer value = Integer.valueOf(request.getTotalAmount());
            Integer newValue = value + 10;
            request.setTotalAmount(preencheCharEsquerda(String.valueOf(newValue), 10, "0"));

            MessageVO body = mobileClubePagTesteService.clubePagRedeemableOfferList(request, token);

            if(body != null){
                transactions.add(body);
            }

        }

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


    @PostMapping(value = "/clubePagConsumerId")
    public ResponseEntity<ClubePagResultVO> clubePagConsumerId(
            @RequestBody AuthenticationRequest request) throws Exception {
        LOGGER.info("ClubePagConsumerId LOAD________________request={}", request);
        String token = service.authenticationRequest(request);
        List<MessageVO> transactions = new ArrayList<>();
        for(int i =0; i < request.getQtd(); i++){
            TransactionResponseVO transaction = service.createTransaction(request, token);
            if(transaction.getTransactionId() != null){
                ClubePagConsumerIdRequest clubePagConsumerIdRequest = request.getClubePagConsumerId();
                clubePagConsumerIdRequest.setTransactionCode(transaction.getTransactionId() );
                clubePagConsumerIdRequest.setApplicationCode(request.getApplicationCode());
                //MessageVO vo = mobileClubePagTesteService.ClubePagConsumerId(clubePagConsumerIdRequest,token);
                Future<MessageVO> future = messageVOAsync(clubePagConsumerIdRequest, token);
                transactions.add(future.get());
            }
            Integer value = Integer.valueOf(request.getEncodeSale().getAmmount());
            Integer newValue = value + 10;
            request.getEncodeSale().setAmmount(preencheCharEsquerda(String.valueOf(newValue), 10, "0"));
        }
        ClubePagResultVO clubePagResultVO = new ClubePagResultVO(transactions);
        return new ResponseEntity<>(clubePagResultVO, HttpStatus.OK);
    }


    private String preencheCharEsquerda(String str, int total, String ch){
        Integer tamanhoString = str.length();
        Integer diferenca = total - tamanhoString;
        String aux = new String();
        for(int i = 0; i < diferenca; i++){
            aux = aux+ch;
        }
        return aux+str;
    }


    public Future<MessageVO> messageVOAsync(ClubePagConsumerIdRequest clubePagConsumerIdRequest, String token) throws InterruptedException {
        LOGGER.info("messageVOAsync________________request={}", clubePagConsumerIdRequest);
        CompletableFuture<MessageVO> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(1000);
            MessageVO vo = mobileClubePagTesteService.ClubePagConsumerId(clubePagConsumerIdRequest,token);
            completableFuture.complete(vo);
            return null;
        });
        return completableFuture;
    }


    public Future<TransactionResponseVO> tansactionResponseVOAsync(AuthenticationRequest request, String token) throws InterruptedException {
        LOGGER.info("TransactionResponseVOAsync  request={}", request);
        CompletableFuture<TransactionResponseVO> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(1000);
            TransactionResponseVO transaction = service.createTransaction(request, token);
            completableFuture.complete(transaction);
            return null;
        });
        return completableFuture;
    }
}
