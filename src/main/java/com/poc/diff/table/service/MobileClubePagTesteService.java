package com.poc.diff.table.service;

import com.google.gson.Gson;
import com.poc.diff.table.http.RestTemplateFactory;
import com.poc.diff.table.http.RestUtil;
import com.poc.diff.table.vo.ClubePagConsumerIdRequest;
import com.poc.diff.table.vo.ClubePagRedeemableOfferListRequest;
import com.poc.diff.table.vo.MessageVO;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 10/12/2021 09:59
 */
@Log4j2
@Service
public class MobileClubePagTesteService {

    private static RestUtil restUtilRunne;

    @Autowired
    RestUtil restUtil;



    public MessageVO clubePagRedeemableOfferList(ClubePagRedeemableOfferListRequest request, String token) throws Exception {
        log.info("ClubePagRedeemableOfferList");
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("type", "0x1040");
        req.put("version", "0x0001");
        req.put("compression", "0x01");
        req.put("enableAccent", "0x01");
        Map<String, String> params = new HashMap<String, String>();
        params.put("TOKEN", token);
        params.put("APPLICATION_CODE", request.getApplicationCode());
        params.put("PHONE_NUMBER", request.getPhoneNumber());
        params.put("TOTAL_AMOUNT", request.getTotalAmount());
        req.put("params", params);

        ResponseEntity<String> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = restUtil.getRestTemplateFactory().getRestTemplate().exchange(
                    restUtil.getEndpoint()+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str,restUtil.getRestTemplateFactory().getHeadersSetToken( token)),
                    String.class);
        } catch (HttpClientErrorException ex) {
            log.error(" error={} ",ex.getMessage());
        }
        return getMessage(response);
    }


    public MessageVO ClubePagConsumerId(ClubePagConsumerIdRequest request, String token) throws Exception {
        log.info("ClubePagConsumerId");
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("type", "0x1080");
        req.put("version", "0x0001");
        req.put("compression", "0x01");
        req.put("enableAccent", "0x01");
        Map<String, String> params = new HashMap<String, String>();
        params.put("TOKEN", token);
        params.put("APPLICATION_CODE", request.getApplicationCode());
        params.put("PHONE_NUMBER", request.getPhoneNumber());
        params.put("TRANSACTION_CODE", request.getTransactionCode());
        req.put("params", params);

        ResponseEntity<String> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = restUtil.getRestTemplateFactory().getRestTemplate().exchange(
                    restUtil.getEndpoint()+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, restUtil.getRestTemplateFactory().getHeadersSetToken( token)),
                    String.class);
        } catch (HttpClientErrorException ex) {
            log.error(" error={} ",ex.getMessage());
        }

        return getMessage(response);
    }

    private MessageVO getMessage(ResponseEntity<String> response){
        JSONObject jsonObject = new JSONObject(response.getBody());
        jsonObject = new JSONObject(jsonObject.get("received").toString());
        jsonObject = new JSONObject(jsonObject.get("params").toString());
        String errorMessage = jsonObject.get("ERROR_MESSAGE[2]").toString();
        String errorCode = jsonObject.get("ERROR_CODE[1]").toString();
        String hostDateTime = jsonObject.get("HOST_DATE_TIME[86]").toString();
        return new MessageVO(errorMessage,errorCode, hostDateTime );
    }



    public String clubePagRedeemableOfferListEstress() throws Exception {
        log.info("clubePagRedeemableOfferListEstress");
        ResponseEntity<String> response = null;
        try {
            response = restUtil.getRestTemplateFactory().getRestTemplate().exchange(
                     "https://clubepag-orchestrator.capture.presential.dev.intranet.pags/redeemable-offer?customerCode=CUSTOMER:A41ECE62B2AC49F0B5ABEAD25136750B&consumerId=11999556633",
                    HttpMethod.GET,
                    null,
                    String.class);
            log.info(response);

            return response.toString();
        } catch (HttpClientErrorException ex) {
            log.error(" error={} ",ex.getMessage());
            return ex.getMessage();
        }
    }

    public static RestUtil getRestUtilRunne(){
        if(restUtilRunne == null){
            restUtilRunne = new RestUtil();
        }
        return restUtilRunne;
    }

    public String clubePagRedeemableOfferListEstressRunnable(int threadNumber) throws Exception {
        log.info("clubePagRedeemableOfferListEstressRunnable "+threadNumber);
        ResponseEntity<String> response = null;
        restUtilRunne = new RestUtil();
        try {
            response = restUtil.getRestTemplateFactory().getRestTemplate().exchange(
                    "https://clubepag-orchestrator.capture.presential.dev.intranet.pags/redeemable-offer?customerCode=CUSTOMER:A41ECE62B2AC49F0B5ABEAD25136750B&consumerId=11999556633",
                    HttpMethod.GET,
                    null,
                    String.class);
            log.info(" threadNumber "+threadNumber+ response);
            return response.toString();
        } catch (HttpClientErrorException ex) {
            log.error(" threadNumber={} error={} ",threadNumber, ex.getMessage());
            return ex.getMessage();
        }
    }
}


