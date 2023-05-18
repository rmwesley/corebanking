package com.example.corebanking;

import com.example.corebanking.dto.ClientCreationDTO;
import com.example.corebanking.model.*;
import com.example.corebanking.repository.ClientRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CorebankingApplicationTests {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  private Client testClient = new Client("John Doe");
  private Account testAccount = new Account(testClient, 100);

  @Autowired private ClientRepository mockClientRepository;

  @Test
  void createdClientTest() {
    mockClientRepository.save(testClient);
    assertTrue(mockClientRepository.findById((long) 1).isPresent());
  }

  @Test
  void createdClientCorrectly() {
    Client saved = mockClientRepository.save(testClient);
    assertEquals(testClient, saved);
    saved = mockClientRepository.findById((long) 1).get();
    assertEquals(testClient.getName(), saved.getName());
  }

  @Test
  void postClientRequestControllerTest() {
    ResponseEntity<Client> responseEntity =
        testRestTemplate.postForEntity(
            "http://localhost:" + port + "/clients",
            new ClientCreationDTO("John Doe"),
            Client.class);
    Client client = responseEntity.getBody();

    assertEquals(201, responseEntity.getStatusCodeValue());
    assertEquals("John Doe", client.getName());
    assertEquals(1, client.getId());
  }

  @Test
  public void testNormalOperations() {
    testAccount.deposit(1.5);
    assertEquals(101.5, testAccount.getBalance());
    testAccount.withdraw(3.3);
    assertEquals(100 + 1.5 - 3.3, testAccount.getBalance());
    testAccount.deposit(5);
    assertEquals(100 + 1.5 - 3.3 + 5, testAccount.getBalance());
  }

  @Test
  public void testThreadSafety() throws InterruptedException {
    Thread depositThread =
        new Thread(
            () -> {
              for (int i = 0; i < 1000; i++) {
                testAccount.deposit(1);
              }
            });
    Thread withdrawThread =
        new Thread(
            () -> {
              for (int i = 0; i < 500; i++) {
                testAccount.withdraw(1);
              }
            });
    depositThread.start();
    depositThread.join();
    assertEquals(1100, testAccount.getBalance());

    withdrawThread.start();
    withdrawThread.join();
    assertEquals(600, testAccount.getBalance());
  }

  @Test
  public void testBarrier() {
    double finalBalance = 100;

    CyclicBarrier barrier =
        new CyclicBarrier(
            3,
            () -> {
              System.out.println("Both transactions finished!");
            });

    Runnable depositTask =
        () -> {
          testAccount.deposit(50);
          System.out.println("Deposit task finished");
          try {
            barrier.await();
          } catch (Exception e) {
            e.printStackTrace();
          }
        };

    Runnable withdrawTask =
        () -> {
          testAccount.withdraw(150);
          System.out.println("Withdraw task finished");
          try {
            barrier.await();
          } catch (Exception e) {
            e.printStackTrace();
          }
        };

    new Thread(depositTask).start();
    new Thread(withdrawTask).start();

    try {
      barrier.await();
    } catch (Exception e) {
      e.printStackTrace();
    }
    finalBalance = testAccount.getBalance();
    System.out.println("Final balance: " + finalBalance);

    assertEquals(0, finalBalance);
  }

  @Test
  public void testLatch() {

    double finalBalance = 100;

    CountDownLatch latch = new CountDownLatch(2);

    Runnable depositTask =
        () -> {
          testAccount.deposit(50);
          System.out.println("Deposit task finished");
          latch.countDown();
        };

    Runnable withdrawTask =
        () -> {
          testAccount.withdraw(150);
          System.out.println("Withdraw task finished");
          latch.countDown();
        };

    new Thread(depositTask).start();
    new Thread(withdrawTask).start();

    try {
      latch.await();
      finalBalance = testAccount.getBalance();
      System.out.println("Final balance: " + finalBalance);
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertEquals(0, finalBalance);
  }

  @Test
  public void testMultithreading() {
    double finalBalance = 100;

    CountDownLatch latch = new CountDownLatch(1);
    CyclicBarrier barrier =
        new CyclicBarrier(
            2,
            () -> {
              System.out.println("Barrier finished!");
            });

    Runnable depositTask =
        () -> {
          testAccount.deposit(50);
          System.out.println("Deposit task finished");
          latch.countDown();
        };

    Runnable withdrawTask =
        () -> {
          testAccount.withdraw(150);
          System.out.println("Withdraw task finished");
          try {
            barrier.await();
          } catch (Exception e) {
            e.printStackTrace();
          }
        };

    new Thread(depositTask).start();
    new Thread(withdrawTask).start();

    try {
      latch.await();
      barrier.await();
      finalBalance = testAccount.getBalance();
      System.out.println("Final balance: " + finalBalance);
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertEquals(0, finalBalance);
  }
}
