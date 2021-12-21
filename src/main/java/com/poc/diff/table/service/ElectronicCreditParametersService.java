package com.poc.diff.table.service;

import com.google.gson.Gson;
import com.poc.diff.table.data.ElectronicCreditParametersData;
import com.poc.diff.table.http.RestUtil;
import com.poc.diff.table.repository.ElectronicCreditCacheRepository;
import com.poc.diff.table.repository.ElectronicCreditParametersDataRepository;
import com.poc.diff.table.repository.ElectronicCreditTableVersionType;
import com.poc.diff.table.vo.AuthenticationRequest;
import com.poc.diff.table.vo.EncodeVO;
import com.poc.diff.table.vo.MessageErrorVO;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class ElectronicCreditParametersService {


    @Autowired
    ElectronicCreditParametersDataRepository electronicCreditParametersDataRepository;

    @Autowired
    ElectronicCreditCacheRepository electronicCreditCacheRepository;


    @Autowired
    RestUtil restUtil;


    public MessageErrorVO copyStableToLatestIfNeeded(AuthenticationRequest request, String token) throws Exception {
        log.info(" copyStableToLatest");
            Long hashStable = 0l;
            Long hashLatest = 0l;
            ElectronicCreditParametersData stableParametersData = getElectronicCreditStableParametersData();

            hashStable = Long.parseLong(stableParametersData.getHash());
            hashStable = hashStable + 1;
            stableParametersData.setHash(hashStable.toString());

            hashLatest = hashStable + 1;

            log.info(" hashStable={} hashLatest={}",hashStable,hashLatest);

            ElectronicCreditParametersData latestParametersData = getElectronicCreditLatestParametersData();
            latestParametersData.setHash(hashLatest.toString());
            latestParametersData.setParameters(stableParametersData.getParameters());

            electronicCreditParametersDataRepository.save(stableParametersData);
            electronicCreditParametersDataRepository.save(latestParametersData);
            electronicCreditCacheRepository.deleteAll();

            String buffer = encodeCargaRecarga( request, token);
            MessageErrorVO vo = inicializacaoTerminal( buffer, token, request.getApplicationCode());
            return vo;
    }


    private void copyLatestToStableIfNeeded () {
            ElectronicCreditParametersData latestParametersData = getElectronicCreditLatestParametersData();
            ElectronicCreditParametersData stableParametersData = new ElectronicCreditParametersData(
                    ElectronicCreditTableVersionType.STABLE.name(),
                    latestParametersData.getVgp(),
                    latestParametersData.getHash(),
                    latestParametersData.getOriginCode(),
                    latestParametersData.getParameters());
    }



    public ElectronicCreditParametersData getElectronicCreditLatestParametersData () {
        return getElectronicCreditParametersData(ElectronicCreditTableVersionType.LATEST);
    }

    public ElectronicCreditParametersData getElectronicCreditBetaParametersData () {
        return getElectronicCreditParametersData(ElectronicCreditTableVersionType.BETA);
    }

    public ElectronicCreditParametersData getElectronicCreditStableParametersData () {
        log.info("getElectronicCreditStableParametersData");
        return getElectronicCreditParametersData(ElectronicCreditTableVersionType.STABLE);
    }

    public ElectronicCreditParametersData getElectronicCreditParametersData (ElectronicCreditTableVersionType type) {
        log.info("getElectronicCreditParametersData type={}", type);
        List<ElectronicCreditParametersData> list = electronicCreditParametersDataRepository.findByTableType(type.name());
        return list.stream().findFirst().get();
    }


    public String encodeCargaRecarga(AuthenticationRequest request, String token) throws Exception {
        log.info("encodeCargaRecarga");

        Map<String, Object> req = new HashMap<String, Object>();
        req.put("_class", "TerminalParametersRequestTag");
        req.put("merchantName", "teste");
        req.put("page", "1");
        req.put("recvBuffSize", "2719");
        req.put("type", "065");
        req.put("pinPadManufacturerName", request.getTerminalParametersRequest().getPinPadManufacturerName());
        req.put("pinPadModel", request.getTerminalParametersRequest().getPinPadModel());
        req.put("pinPadSerialNumber", request.getSerialNumber());
        req.put("electronicCreditTableVersion",  "ABC");
        req.put("pinPadAppSpecVersion",  "123");
        req.put("tableRegistersLimit",  "0");
        req.put("libVersion",  "123");
        req.put("protocolVersion",  "123");
        ResponseEntity<EncodeVO> response = null;
        Gson g = new Gson();
        String str = g.toJson(req);
        try {
            response = restUtil.getRestTemplateFactory().getRestTemplate().exchange(
                    restUtil.getEndpoint()+"/utilTLV",
                    HttpMethod.POST,
                    new HttpEntity<>(str, restUtil.getRestTemplateFactory().getHeadersSetToken(token)),
                    EncodeVO.class);
        } catch (HttpClientErrorException ex) {
            log.error(" error={} ",ex.getMessage());
        }
        log.info("buffer={}", response.getBody().getBuffer());
        return response.getBody().getBuffer();
    }




    public MessageErrorVO inicializacaoTerminal(String buffer, String token, String applicationCode){
        log.info("Inicialização Terminal");

        Map<String, Object> req = new HashMap<String, Object>();
        req.put("type", "0x0800");
        req.put("version", "0x0001");
        req.put("compression", "0x01");
        req.put("enableAccent", "0x01");

        Map<String, String> params = new HashMap<String, String>();
        params.put("TOKEN", token);
        params.put("APPLICATION_CODE", applicationCode);
        params.put("BUFFER", buffer);
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

        } catch (Exception e) {
            log.error(" error={} ",e.getMessage());
        }
        MessageErrorVO errorVO = getMessageError(response);

        return errorVO;
    }

    private MessageErrorVO getMessageError(ResponseEntity<String> response){
        log.info("getMessageError={}",response.getBody());
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
        log.info("errorVO={}",errorVO);
        return errorVO;
    }

}







