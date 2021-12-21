package com.poc.diff.table.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ileonardo
 * @since 21/12/2021 08:40
 */
@Component
public class RestUtil {

    private RestTemplateFactory restTemplateFactory;

    @Value("${url.mobile.stg}")
    private String endpointStg;

    @Value("${url.mobile.qa}")
    private String endpointQa;

    @Value("${env}")
    private String env;

    public String getEndpoint(){
        if(env.equalsIgnoreCase("stg")){
            return endpointStg;
        }else{
            return endpointQa;
        }
    }

    public RestTemplateFactory getRestTemplateFactory() throws Exception {
        if (this.restTemplateFactory == null) {
            this.restTemplateFactory = new RestTemplateFactory();
        }
        return this.restTemplateFactory;
    }

}
