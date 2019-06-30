package ru.test.account.api;

import org.junit.Test;
import ru.test.account.api.dto.StatusResponse;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Manual load test. Start this test on just started application. Should work one time.
 */
public class ApiLtIT {

    private static WebTarget target = ClientBuilder.newClient().target(System.getProperty("url", "http://localhost:8080/"));
    private static Map<String, AtomicInteger> accounts = new ConcurrentHashMap<>();


    @Test
    public void testAll() {
        System.out.println("start create accounts");
        generateSums();
        System.out.println("accounts created " + accounts.size());
        System.out.println("start check accounts");
        checkSums();
        System.out.println("accounts checked");
        System.out.println("start 1000 operations");
        IntStream.range(0, 1000).parallel().forEach((i) -> {
            String account1 = String.format("%05d", new Random().nextInt(10000));
            String account2 = String.format("%05d", new Random().nextInt(10000));
            AtomicInteger sum1 = accounts.get(account1);
            AtomicInteger sum2 = accounts.get(account2);
            sum1.decrementAndGet();
            sum2.incrementAndGet();
            assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer(account1, account2, 1, target));
        });
        System.out.println("start check accounts");
        checkSums();
        System.out.println("accounts checked");

    }

    private static void generateSums() {
        IntStream.range(0, 10000).parallel().forEach((i) -> {
            String account = String.format("%05d", i);
            assertEquals(StatusResponse.OK, ManualClientHelper.createAccount(account, 1000, target));
            accounts.put(account, new AtomicInteger(1000));

        });
    }

    private static void checkSums() {
        accounts.entrySet().stream().forEach((i) -> {
            String account = i.getKey();
            int sum = ManualClientHelper.checkAccount(account, target);
            assertEquals(i.getValue().get(), sum);
        });
    }



}
