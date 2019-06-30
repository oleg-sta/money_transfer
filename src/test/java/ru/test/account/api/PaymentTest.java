package ru.test.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ru.test.account.api.dto.StatusResponse;
import ru.test.account.api.dto.TransferDTO;
import ru.test.account.api.dto.TransferResponse;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.TransferEntity;
import ru.test.account.service.TransferService;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaymentTest {
    ObjectMapper mapper = new ObjectMapper();
    @Test
    public void transferMoneySuccess() throws Exception {
        Payment payment = new Payment();
        TransferService transferService = mock(TransferService.class);
        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                ((TransferEntity)args[0]).setId(111);
                return StatusResponse.OK;
            }
        }).when(transferService).transferMoney(any());
        payment.transferService = transferService;
        Response response = payment.transferMoney(new TransferDTO("11111", "11123", 12));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TransferResponse transferResponse = mapper.readValue(response.getEntity().toString(), TransferResponse.class);
        assertEquals(StatusResponse.OK, transferResponse.getStatusResponse());
        assertEquals(111, transferResponse.getId());
    }

    @Test
    public void transferMoneyBusinessError() throws Exception {
        Payment payment = new Payment();
        TransferService transferService = mock(TransferService.class);
        when(transferService.transferMoney(any())).thenThrow(new BusinessException("some error", 4));
        payment.transferService = transferService;
        Response response = payment.transferMoney(new TransferDTO("11111", "11123", 12));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TransferResponse transferResponse = mapper.readValue(response.getEntity().toString(), TransferResponse.class);
        assertEquals(4, transferResponse.getStatusResponse());
    }

    @Test
    public void transferMoneyOtherError() throws Exception {
        Payment payment = new Payment();
        TransferService transferService = mock(TransferService.class);
        when(transferService.transferMoney(any())).thenThrow(new RuntimeException("some error"));
        payment.transferService = transferService;
        Response response = payment.transferMoney(new TransferDTO("11111", "11123", 12));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }



}