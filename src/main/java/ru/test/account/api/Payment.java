package ru.test.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.test.account.api.dto.AccountResponse;
import ru.test.account.api.dto.StatusResponse;
import ru.test.account.api.dto.TransferDTO;
import ru.test.account.api.dto.TransferResponse;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.TransferEntity;
import ru.test.account.service.TransferService;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/payment")
@Singleton
public class Payment {

    ObjectMapper mapper = new ObjectMapper();
    TransferService transferService = new TransferService();

    @POST
    @Path("transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferMoney(TransferDTO transfer) {
        TransferEntity transferEntity = new TransferEntity(transfer.getAccount1(), transfer.getAccount2(), transfer.getAmount());
        try {
            try {
                transferService.transferMoney(transferEntity);
                return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new TransferResponse(StatusResponse.OK, transferEntity.getId()))).build();
            } catch (BusinessException e) {
                e.printStackTrace();
                return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(new TransferResponse(e.getCode(), 0))).build();
            }
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
