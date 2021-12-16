# diff-new_electronic_credit_parameters


Salve o resultado do curl:

curl GET ../commands/electronicCredit/tableLoad/getAll
Em um arquivo .txt na sua maquina, execute o start da aplicação:


Endpoint:

http://localhost:8080/switch_new_electronic_credit_parameters/diff

payload:
{
    "repositoryPath": "/home/ileonardo/Documents/Sustentaçao/Recarga/2021" ,
    "folderName":  "SDPE-651662"
}


//Recarga Celular



///Transação Recarga *QA

http://localhost:8080/mobile/transaction

{
"applicationCode": "566210",
"readerModel": "M30",
"serialNumber": "68003142",
"activationCode": "749879",
"encodeSale":{   
"pinPadManufacturerName": "PAX                 ",
"cardTransactionType": 1,
"ammount": "0000012400",
"pan": "5521280901504768" ,
"electronicCreditBranchCode": "00201000000",
"electronicCreditPhoneNumber": "31999999999"
}
}




///Transação Recarga por Quantidade *QA

http://localhost:8080/mobile/transaction-load
{
"qtd": 1,
"applicationCode": "566210",
"readerModel": "M30",
"serialNumber": "68003142",
"activationCode": "749879",
"encodeSale":{   
"pinPadManufacturerName": "PAX                 ",
"cardTransactionType": 1,
"ammount": "0000006600",
"pan": "4556779321370057",
"electronicCreditBranchCode": "00201000000",
"electronicCreditPhoneNumber": "31999999999"
}
}





//ClubePag

//Registrar

http://localhost:8080/mobile/clubePagConsumerId

{
"qtd": 20,
"applicationCode": "566210",
"readerModel": "M30",
"serialNumber": "68003142",
"activationCode": "749879",
"encodeSale":{   
"pinPadManufacturerName": "PAX                 ",
"cardTransactionType": 1,
"ammount": "0000003600",
"pan": "5521280901504768"
},
"clubePagConsumerId": {
"phoneNumber": "11963852741",
"activationCode": "749879"
}   
}



/// Lista de Ofertas

http://localhost:8080/mobile/clubePagRedeemableOfferList

{
"qtd": 1,
"applicationCode": "566210",
"readerModel": "M30",
"serialNumber": "68003142",
"activationCode": "749879",
"phoneNumber": "11963852741",
"totalAmount": "100",
"encodeSale":{   
"pinPadManufacturerName": "PAX                 ",
"cardTransactionType": 1,
"ammount": "0000002600",
"pan": "4556779321370057"
}   
}





//STG
{
"qtd": 1,
"applicationCode": "566210",
"readerModel": "PAX_S920_BGW",
"serialNumber": "68560184",
"activationCode": "403938",
"encodeSale":{   
"pinPadManufacturerName": "PAX                 ",
"cardTransactionType": 1,
"ammount": "0000006600",
"pan": "4556779321370057",
"electronicCreditBranchCode": "00201000000",
"electronicCreditPhoneNumber": "31999999999"
}
}

 