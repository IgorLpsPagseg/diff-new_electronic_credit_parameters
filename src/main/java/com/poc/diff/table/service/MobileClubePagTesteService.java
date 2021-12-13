package com.poc.diff.table.service;

import com.google.gson.Gson;
import com.poc.diff.table.http.RestTemplateFactory;
import com.poc.diff.table.vo.ClubePagConsumerIdRequest;
import com.poc.diff.table.vo.ClubePagRedeemableOfferListRequest;
import com.poc.diff.table.vo.MessageVO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class MobileClubePagTesteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileClubePagTesteService.class);

    private static final String urlTeste ="http://d3-hispania-qa2.host.intranet:34069/admin/service";

    private RestTemplateFactory restTemplateFactory;

    public RestTemplateFactory getRestTemplateFactory() throws Exception {
        if (this.restTemplateFactory == null) {
            this.restTemplateFactory = new RestTemplateFactory();
        }
        return this.restTemplateFactory;
    }


    public MessageVO clubePagRedeemableOfferList(ClubePagRedeemableOfferListRequest request, String token) throws Exception {
        LOGGER.info("ClubePagRedeemableOfferList");
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
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    urlTeste+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str,getRestTemplateFactory().getHeadersSetToken( token)),
                    String.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
        }
        return getMessage(response);
    }


    public MessageVO ClubePagConsumerId(ClubePagConsumerIdRequest request, String token) throws Exception {
        LOGGER.info("ClubePagConsumerId");
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
            response = getRestTemplateFactory().getRestTemplate().exchange(
                    urlTeste+"/callTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, getRestTemplateFactory().getHeadersSetToken( token)),
                    String.class);
        } catch (HttpClientErrorException ex) {
            LOGGER.error(" error={} ",ex.getMessage());
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
}
