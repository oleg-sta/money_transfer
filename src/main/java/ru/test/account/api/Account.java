package ru.test.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.test.account.api.dto.AccountDTO;
import ru.test.account.api.dto.AccountResponse;
import ru.test.account.api.dto.StatusResponse;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.AccountEntity;
import ru.test.account.service.AccountService;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/account")
@Singleton
public class Account {

    ObjectMapper mapper = new ObjectMapper();

    AccountService accountService = new AccountService();

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountDTO account) {
        AccountEntity accountEntity = new AccountEntity(account.getName(), account.getSum());
        try {
            try {
                accountService.createAccount(accountEntity);
            } catch (BusinessException e) {
                e.printStackTrace();
                return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new AccountResponse(e.getCode(), "", 0))).build();
            }
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new AccountResponse(StatusResponse.OK, accountEntity.getName(), accountEntity.getSum()))).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/info/{account}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getInfoAccount(@PathParam("account") String account) {
        try {
            try {
                AccountEntity accountEntity = accountService.getAccountInfo(account);
                return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new AccountResponse(StatusResponse.OK, accountEntity.getName(), accountEntity.getSum()))).build();
            } catch (BusinessException e) {
                e.printStackTrace();
                return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new AccountResponse(e.getCode(), "", 0))).build();
            }
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
