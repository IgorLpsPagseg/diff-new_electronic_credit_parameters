package com.poc.diff.table.resource;

import com.poc.diff.table.service.MobileClubePagTesteService;
import com.poc.diff.table.service.MobileService;
import com.poc.diff.table.vo.AuthenticationRequest;
import com.poc.diff.table.vo.ClubePagConsumerIdRequest;
import com.poc.diff.table.vo.ClubePagRedeemableOfferListRequest;
import com.poc.diff.table.vo.ClubePagResultVO;
import com.poc.diff.table.vo.MessageVO;
import com.poc.diff.table.vo.TransactionResponseVO;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author ileonardo
 * @since 16/11/2021 17:26
 */
@Log4j2
@RestController
@RequestMapping(path = "mobile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class MobileTesteResource {


    @Autowired
    private MobileService service;

    @Autowired
    private MobileClubePagTesteService mobileClubePagTesteService;


    @PostMapping(value = "/transaction")
    public ResponseEntity<TransactionResponseVO> transaction(@RequestBody AuthenticationRequest request) throws Exception {
        log.info("transaction");
        String token = service.authenticationRequest(request);
        return new ResponseEntity<>(service.createTransaction(request, token), HttpStatus.OK);
    }


    @PostMapping(value = "/transaction-load")
    public ResponseEntity<List<TransactionResponseVO>> transactionLoad(@RequestBody AuthenticationRequest request) throws Exception {
        log.info("transactionLoad");
        List<TransactionResponseVO> transactions = new ArrayList<>();
        String token = service.authenticationRequest(request);
        for (int i = 0; i < request.getQtd(); i++) {
            TransactionResponseVO transaction = service.createTransaction(request, token);
//            Future<TransactionResponseVO> futureVO =  tansactionResponseVOAsync(request, token);
//            TransactionResponseVO transaction = futureVO.get();

            if (transaction.getTransactionId() != null) {
                transactions.add(transaction);
            }

            Integer value = Integer.valueOf(request.getEncodeSale().getAmmount());
            Integer newValue = value + 10;
            request.getEncodeSale().setAmmount(preencheCharEsquerda(String.valueOf(newValue), 10, "0"));
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping(value = "/clubePagRedeemableOfferList")
    public ResponseEntity<List<MessageVO>> clubePagRedeemableOfferList(@RequestBody ClubePagRedeemableOfferListRequest request) throws Exception {
        log.info("clubePagRedeemableOfferList ");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setActivationCode(request.getActivationCode());
        authenticationRequest.setApplicationCode(request.getApplicationCode());
        authenticationRequest.setReaderModel(request.getReaderModel());
        authenticationRequest.setSerialNumber(request.getSerialNumber());
        String token = service.authenticationRequest(authenticationRequest);
        List<MessageVO> transactions = new ArrayList<>();

        for (int i = 0; i < request.getQtd(); i++) {

            Integer value = Integer.valueOf(request.getTotalAmount());
            Integer newValue = value + 10;
            request.setTotalAmount(preencheCharEsquerda(String.valueOf(newValue), 10, "0"));

            MessageVO body = mobileClubePagTesteService.clubePagRedeemableOfferList(request, token);

            if (body != null) {
                transactions.add(body);
            }

        }

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }



    @PostMapping(value = "/clubePagRedeemableOfferListEstress")
    public ResponseEntity<List<String >> clubePagRedeemableOfferListEstress(@RequestBody ClubePagRedeemableOfferListRequest request) throws Exception {
        log.info("clubePagRedeemableOfferListEstress ");
        List<String> transactions = new ArrayList<>();
        for (int i = 0; i < request.getQtd(); i++) {
            String  body = mobileClubePagTesteService.clubePagRedeemableOfferListEstress();
            transactions.add(body);

        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


    @PostMapping(value = "/clubePagRedeemableOfferListThread")
    public ResponseEntity<Void> clubePagRedeemableOfferListThread(@RequestBody ClubePagRedeemableOfferListRequest request) throws Exception {
        log.info("clubePagRedeemableOfferListThread ");
            TarefaMultiplicacao tarefa = new TarefaMultiplicacao(request.getQtd());
            Thread threadMultiplicador = new Thread(tarefa);
            threadMultiplicador.start();
        return new ResponseEntity<>( HttpStatus.OK);
    }

    class TarefaMultiplicacao implements Runnable {

        private Integer qtd;

        public TarefaMultiplicacao(Integer qtd) {
            this.qtd = qtd;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < this.qtd ; i++) {
                TarefaCall tarefaCall = new TarefaCall(i);
                Thread threadMultiplicador = new Thread(tarefaCall);
                threadMultiplicador.start();
            }
        }
    }

    class TarefaCall implements Runnable {
        int threadNumber;

        TarefaCall(int threadNumber){
            this.threadNumber = threadNumber;
        }
        @SneakyThrows
        @Override
        public void run() {
           mobileClubePagTesteService.clubePagRedeemableOfferListEstressRunnable(this.threadNumber);
        }
    }


    @PostMapping(value = "/clubePagConsumerId")
    public ResponseEntity<ClubePagResultVO> clubePagConsumerId(
            @RequestBody AuthenticationRequest request) throws Exception {
        log.info("ClubePagConsumerId LOAD");
        String token = service.authenticationRequest(request);
        List<MessageVO> transactions = new ArrayList<>();
        for (int i = 0; i < request.getQtd(); i++) {
            TransactionResponseVO transaction = service.createTransaction(request, token);
            if (transaction.getTransactionId() != null) {
                ClubePagConsumerIdRequest clubePagConsumerIdRequest = request.getClubePagConsumerId();
                clubePagConsumerIdRequest.setTransactionCode(transaction.getTransactionId());
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


    private String preencheCharEsquerda(String str, int total, String ch) {
        Integer tamanhoString = str.length();
        Integer diferenca = total - tamanhoString;
        String aux = new String();
        for (int i = 0; i < diferenca; i++) {
            aux = aux + ch;
        }
        return aux + str;
    }


    public Future<MessageVO> messageVOAsync(ClubePagConsumerIdRequest clubePagConsumerIdRequest, String token) throws InterruptedException {
        log.info("messageVOAsync ");
        CompletableFuture<MessageVO> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(1000);
            MessageVO vo = mobileClubePagTesteService.ClubePagConsumerId(clubePagConsumerIdRequest, token);
            completableFuture.complete(vo);
            return null;
        });
        return completableFuture;
    }


    public Future<TransactionResponseVO> tansactionResponseVOAsync(AuthenticationRequest request, String token) throws InterruptedException {
        log.info("tansactionResponseVOAsync");
        CompletableFuture<TransactionResponseVO> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            //Thread.sleep(500);
            TransactionResponseVO transaction = service.createTransaction(request, token);
            completableFuture.complete(transaction);
            return null;
        });
        return completableFuture;
    }


    @PostMapping(value = "/last-date")
    public ResponseEntity<String> lastDate(@RequestBody AuthenticationRequest request) throws Exception {

        Integer quantity = request.getQtd();
        Date lastExecutionDate =  new Date();
        Date startDate  = getStartDateSearch(  lastExecutionDate, - quantity);

        Integer quantity2 = Integer.parseInt(request.getSerialNumber());
        Date lastExecution =    getStartDateSearch(   new Date(), - quantity2);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String requiredDate = df.format(startDate);

        String requiredDatelastExecution = df.format(lastExecution);


        String result = " start: "+requiredDate+ " last: "+requiredDatelastExecution;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    Date getStartDateSearch(Date lastExecutionDate, Integer quantity) {
        return DateUtils.addDays(lastExecutionDate, quantity);
    }

}
