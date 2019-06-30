package ru.test.account.api;

import ru.test.account.api.dto.AccountDTO;
import ru.test.account.api.dto.AccountResponse;
import ru.test.account.api.dto.TransferDTO;
import ru.test.account.api.dto.TransferResponse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ManualClientHelper {
    public static int checkAccount(String s, WebTarget target) {
        AccountResponse result = target.path("account/info/" + s).request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(AccountResponse.class);
        return result.getSum();
    }

    public static int createAccount(String account, int value, WebTarget target) {
        AccountResponse result =  target.path("account/create").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(new AccountDTO(account, value)), AccountResponse.class);
        return  result.getStatusResponse();
    }

    public static int createTransfer(String account1, String account2, int value, WebTarget target) {
        TransferResponse transferResponse = target.path("payment/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new TransferDTO(account1, account2, value)), TransferResponse.class);
        return  transferResponse.getStatusResponse();
    }
}
