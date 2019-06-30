package ru.test.account.api;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.test.account.api.dto.StatusResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Manual It test for finding probable problems in multithreading. The application should be just started up. Each test works only one time, the second time couldn't be work
 */
public class MultiThreadCheckIT {
    private static WebTarget target;

    @BeforeClass
    public static void init() {
        Client client = ClientBuilder.newClient();
        target = client.target(System.getProperty("url", "http://localhost:8080/"));
    }

    @Test
    public void findPossibleProblemsOnTwoAccountsMultithread() throws InterruptedException {
        String account1 = "11131";
        String account2 = "11132";
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount(account1, 1000, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount(account2, 1000, target));
        ExecutorService exService = Executors.newFixedThreadPool(2);
        List<Callable<Object>> jobs = new ArrayList<Callable<Object>>();
        jobs.add(() -> {
            for (int i = 0; i < 1000; i++) {
                assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer(account1, account2, 1, target));
            }
            return null;
        });
        jobs.add(() -> {
            for (int i = 0; i < 800; i++) {
                assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer(account2, account1, 1, target));
            }
            return null;
        });
        exService.invokeAll(jobs);
        assertEquals(800, ManualClientHelper.checkAccount(account1, target));
        assertEquals(1200, ManualClientHelper.checkAccount(account2, target));
    }

    @Test
    public void findPossibleProblemsOnThreeAccountsMultithread() throws InterruptedException {
        String account1 = "11133";
        String account2 = "11134";
        String account3 = "11135";
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount(account1, 2000, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount(account2, 1000, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount(account3, 1000, target));
        ExecutorService exService = Executors.newFixedThreadPool(3);
        List<Callable<Object>> jobs = new ArrayList<Callable<Object>>();
        jobs.add(() -> {
            for (int i = 0; i < 1000; i++) {
                assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer(account1, account2, 1, target));
            }
            return null;
        });
        jobs.add(() -> {
            for (int i = 0; i < 800; i++) {
                assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer(account2, account1, 1, target));
            }
            return null;
        });
        jobs.add(() -> {
            for (int i = 0; i < 700; i++) {
                assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer(account1, account3, 1, target));
            }
            return null;
        });
        exService.invokeAll(jobs);
        assertEquals(1100, ManualClientHelper.checkAccount(account1, target));
        assertEquals(1200, ManualClientHelper.checkAccount(account2, target));
        assertEquals(1700, ManualClientHelper.checkAccount(account3, target));
    }

}
