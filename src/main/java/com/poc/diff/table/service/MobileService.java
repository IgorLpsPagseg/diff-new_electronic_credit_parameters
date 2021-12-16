package com.poc.diff.table.service;

import com.google.gson.Gson;
import com.poc.diff.table.http.RestTemplateFactory;
import com.poc.diff.table.vo.AuthenticationRequest;
import com.poc.diff.table.vo.EncodeVO;
import com.poc.diff.table.vo.MessageErrorVO;
import com.poc.diff.table.vo.TransactionCreditCardSaleResul;
import com.poc.diff.table.vo.TransactionResponse;
import com.poc.diff.table.vo.TransactionResponseVO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ileonardo
 * @since 09/12/2021 09:50
 */
@Service
public class MobileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileService.class);


    @Value("${url.mobile.qa}")
    private String endpoint;


    private RestTemplateFactory restTemplateFactory;

    public RestTemplateFactory getRestTemplateFactory() throws Exception {
        if (this.restTemplateFactory == null) {
            this.restTemplateFactory = new RestTemplateFactory();
        }
        return this.restTemplateFactory;
    }


    public TransactionResponseVO createTransaction(AuthenticationRequest request, String token) throws Exception {
        String buffer = encodeSale(request,token);
        TransactionCreditCardSaleResul resul = transactionCreditCardSale(buffer, token, request.getApplicationCode());

        if (!resul.getMessageErrorVO().getCode().equalsIgnoreCase("0000")){
            LOGGER.error(" errorVO={} ",resul.getMessageErrorVO());
            return new TransactionResponseVO("null", resul.getMessageErrorVO().getCode(), "null",resul.getMessageErrorVO().getDescription());
        }

        String  transactionCreditCardSale = resul.getBuffer37();
        String transactionId = switchBufferUtilityDecodeBuffer(transactionCreditCardSale, token);
        String switchBufferUtilityEncodeConfirmation = switchBufferUtilityEncodeConfirmation(transactionId, token);
        String transactionCreditConfirmation = transactionCreditConfirmation(switchBufferUtilityEncodeConfirmation, token);
        String errorCode = getErrorCode(transactionCreditConfirmation);
        return new TransactionResponseVO(transactionId, errorCode, transactionCreditConfirmation, resul.getMessageErrorVO().getDescription());
    }

    public String authenticationRequest(AuthenticationRequest request){
        LOGGER.info("Authentication Request");

        Map<String, String> params = new HashMap<String, String>();
        params.put("APPLICATION_CODE", request.getApplicationCode());
        params.put("READER_MODEL", request.getReaderModel());
        params.put("SERIAL_NUMBER", request.getSerialNumber());
        params.put("ACTIVATION_CODE", request.getActivationCode());

        Map<String, Object> req = new HashMap<String, Object>();
        req.put("type", "0x0100");
        req.put("version", "0x0001");
        req.put("compression", "0x01");
        req.put("gatewayType", "0x01");
        req.put("enableAccent", "0x01");
        req.put("params", params);

        ResponseEntity<String> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    endpoint+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeaders()),
                    String.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
        } catch (Exception e) {
            LOGGER.error(" error={} ",e.getMessage());
        }
        return getToken( response);
    }


    private String preencheVazio(String str, int total){
       Integer tamanhoString = str.length();
       Integer diferenca = total - tamanhoString;
       for(int i =0; i < diferenca; i++){
           str = str+" ";
       }
       return str;
    }

    public String encodeSale(AuthenticationRequest request, String token) throws Exception {
        LOGGER.info("EncodeSale ");
        String pinPadSerialNumber = null;
        String pinPadModel = request.getReaderModel();
        if(request.getEncodeSale().getPinPadSerialNumber() == null)
        {
            pinPadSerialNumber = preencheVazio(request.getSerialNumber(), 20);
        }
        if(request.getEncodeSale().getPinPadManufacturerName() == null)
        {
           // LOGGER.info("preencheVazio ");
            pinPadModel= preencheVazio( request.getReaderModel(), 19);
         //   LOGGER.info("pinPadModel={} ",pinPadModel);
        }


        Map<String, Object> req = new HashMap<String, Object>();
        req.put("_class", "SaleRequestTag");
        req.put("pinPadAppSpecVersion", "1.08");
        req.put("pinPadSerialNumber", pinPadSerialNumber);//20 carcteres
        req.put("pinPadManufacturerName", request.getEncodeSale().getPinPadManufacturerName());
        req.put("pinPadModel", pinPadModel);
        req.put("cardTransactionType", request.getEncodeSale().getCardTransactionType());
        req.put("ammount", request.getEncodeSale().getAmmount());
        req.put("pan", request.getEncodeSale().getPan());
        req.put("track2Data", request.getEncodeSale().getPan()+"=18112060000987609501");
        req.put("emvParameters", "9F2608220F16C2CEAAB3829F2701809F10120310A50002020000000000000000000000FF9F370459473EF59F3602027D950540000000009A031509249C01009F02060000000004809F03060000000000005F2A020986820258009F1A0200769F34034103025F24032212319F150200009F3303E0F0C85F280200768407A0000000041010");
        req.put("cardInputMode",  "1");
        req.put("cardExpirationDate",  "1811");
        req.put("nsuTerminal",  "168");
        req.put("pinPadAcquirerCode",  "04");
        req.put("installmentType",  "1");
        req.put("installmentNumber",  "1");
        req.put("panSequence",  "00");
        req.put("currencyCode",  "986");
        req.put("cardHolderName",  "FABIANO PAES L FREIRE     ");
        req.put("productId",  "0"+request.getEncodeSale().getCardTransactionType());
        req.put("passwordVerificationAndCaptureMode",  "3");

        req.put("protocolVersion",  "00.00.90");

        if(request.getEncodeSale().getElectronicCreditBranchCode() != null){
            req.put("electronicCreditBranchCode",  request.getEncodeSale().getElectronicCreditBranchCode());
            req.put("electronicCreditPhoneNumber",  request.getEncodeSale().getElectronicCreditPhoneNumber());
        }

        //LOGGER.info("EncodeSale req={}",req);

        ResponseEntity<EncodeVO> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    endpoint+"/utilTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeadersSetToken( token)),
                    EncodeVO.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
        }

        LOGGER.info("buffer={}", response.getBody().getBuffer());
        return response.getBody().getBuffer();
    }


    public TransactionCreditCardSaleResul transactionCreditCardSale(String buffer, String token, String applicationCode){
        LOGGER.info("Transaction Credit Card Sale");

        Map<String, Object> req = new HashMap<String, Object>();
        req.put("type", "0x0200");
        req.put("version", "0x0001");
        req.put("compression", "0x01");
        req.put("gatewayType", "0x01");
        req.put("enableAccent", "0x01");

        Map<String, String> params = new HashMap<String, String>();
        params.put("TOKEN", token);
        params.put("APPLICATION_CODE", applicationCode);
        params.put("REFERENCE", "1434479664880");
        params.put("FIRMWARE", "123456");
        params.put("LIBRARY", "654321");
        params.put("INSTALLMENT_TYPE", "01");
        params.put("SIM_CARD_SERIAL", "8991101200003204510");
        params.put("BUFFER", buffer);

        req.put("params", params);

        ResponseEntity<String> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    endpoint+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeadersSetToken( token)),
                    String.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());

        } catch (Exception e) {
            LOGGER.error(" error={} ",e.getMessage());
        }
        MessageErrorVO errorVO = getMessageError(response);

        return  new TransactionCreditCardSaleResul(getBUFFER37(response), errorVO);
    }


    public String switchBufferUtilityDecodeBuffer(String buffer37, String token) throws Exception {
        LOGGER.info("Switch Buffer Utility -- Decode Buffer");
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("BUFFER", buffer37);

        ResponseEntity<TransactionResponse> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    endpoint+"/utilTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeadersSetToken( token)),
                    TransactionResponse.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
        }
        LOGGER.info("transactionId={}", response.getBody().getTransactionId());
        return response.getBody().getTransactionId();
    }


    public String switchBufferUtilityEncodeConfirmation(String transactionId, String token) throws Exception {
        LOGGER.info("Switch Buffer Utility --Encode Confirmation");
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("_class", "SaleConfirmationRequestTag");
        req.put("transactionId", transactionId);
        req.put("emvParameters", null);
        req.put("issuerScriptResults", null);
        req.put("terminalResponseCode", "0");
        req.put("nsuTerminal", "22648");
        req.put("protocolVersion", "00.00.90");

        ResponseEntity<EncodeVO> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    endpoint+"/utilTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeadersSetToken( token)),
                    EncodeVO.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
        }
        LOGGER.info("body={}", response.getBody());
        return response.getBody().getBuffer();
    }



    public String transactionCreditConfirmation(String buffer, String token) throws Exception {
        LOGGER.info("Transaction Credit Card  -- Confirmation");

        Map<String, Object> req = new HashMap<String, Object>();
        req.put("type", "0x0202");
        req.put("version", "0x0001");
        req.put("compression", "0x01");
        req.put("enableAccent", "0x01");

        Map<String, String> params = new HashMap<String, String>();
        params.put("TOKEN", token);
        params.put("BUFFER", buffer);
        req.put("params", params);

        ResponseEntity<String> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    endpoint+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeadersSetToken( token)),
                    String.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
        }
        return response.getBody();
    }


    public String getErrorCode(String response){
        JSONObject jsonObject = new JSONObject(response);
        jsonObject = new JSONObject(jsonObject.get("received").toString());
        jsonObject = new JSONObject(jsonObject.get("params").toString());
        LOGGER.info("ERROR_CODE[1]={}",jsonObject.get("ERROR_CODE[1]"));
        return jsonObject.get("ERROR_CODE[1]").toString();
    }


    private String getBUFFER37(ResponseEntity<String> response){
        JSONObject jsonObject = new JSONObject(response.getBody());
        jsonObject = new JSONObject(jsonObject.get("received").toString());
        jsonObject = new JSONObject(jsonObject.get("params").toString());
        LOGGER.info("BUFFER[37]={}",jsonObject.get("BUFFER[37]"));
        return jsonObject.get("BUFFER[37]").toString();
    }

    private MessageErrorVO getMessageError(ResponseEntity<String> response){
        LOGGER.info("getMessageError={}",response.getBody());
        JSONObject jsonObject = new JSONObject(response.getBody());
        jsonObject = new JSONObject(jsonObject.get("received").toString());
        jsonObject = new JSONObject(jsonObject.get("params").toString());
        String errormessage = null;


        if(!jsonObject.get("ERROR_CODE[1]").toString().equalsIgnoreCase("0000")){
            errormessage = jsonObject.get("ERROR_MESSAGE[2]").toString();
        }else {
            errormessage = null;
        }

        MessageErrorVO errorVO = new MessageErrorVO(jsonObject.get("ERROR_CODE[1]").toString(),errormessage );
        LOGGER.info("errorVO={}",errorVO);
        return errorVO;
    }

    private String getToken(ResponseEntity<String> response){
        JSONObject jsonObject = new JSONObject(response.getBody());
        jsonObject = new JSONObject(jsonObject.get("received").toString());
        jsonObject = new JSONObject(jsonObject.get("params").toString());
        LOGGER.info(" token={}", jsonObject.get("TOKEN[7]"));
        return jsonObject.get("TOKEN[7]").toString();
    }
}
