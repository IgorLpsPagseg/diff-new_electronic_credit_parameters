package com.poc.diff.table.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

/**
 * @author ileonardo
 * @since 09/12/2021 10:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class EncodeSaleRequest {

    String pinPadSerialNumber;
    String pinPadManufacturerName;
    String pinPadModel;
    String cardTransactionType;
    String ammount;
    String pan;
    String electronicCreditBranchCode;
    String electronicCreditPhoneNumber;


    public String getPinPadSerialNumber() {
        return pinPadSerialNumber;
    }

    public void setPinPadSerialNumber(String pinPadSerialNumber) {
        this.pinPadSerialNumber = pinPadSerialNumber;
    }

    public String getPinPadManufacturerName() {
        return pinPadManufacturerName;
    }

    public void setPinPadManufacturerName(String pinPadManufacturerName) {
        this.pinPadManufacturerName = pinPadManufacturerName;
    }

    public String getPinPadModel() {
        return pinPadModel;
    }

    public void setPinPadModel(String pinPadModel) {
        this.pinPadModel = pinPadModel;
    }

    public String getCardTransactionType() {
        return cardTransactionType;
    }

    public void setCardTransactionType(String cardTransactionType) {
        this.cardTransactionType = cardTransactionType;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getElectronicCreditBranchCode() {
        return electronicCreditBranchCode;
    }

    public void setElectronicCreditBranchCode(String electronicCreditBranchCode) {
        this.electronicCreditBranchCode = electronicCreditBranchCode;
    }

    public String getElectronicCreditPhoneNumber() {
        return electronicCreditPhoneNumber;
    }

    public void setElectronicCreditPhoneNumber(String electronicCreditPhoneNumber) {
        this.electronicCreditPhoneNumber = electronicCreditPhoneNumber;
    }
}
