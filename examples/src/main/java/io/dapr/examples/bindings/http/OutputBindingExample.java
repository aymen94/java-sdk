/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */

package io.dapr.examples.bindings.http;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;

/**
 * Service for output binding example.
 * 1. From your repo root, build and install jars:
 * mvn clean install
 * 2. cd to [repo-root]/examples
 * 3. Run the program:
 * dapr run --app-id outputbinding --port 3006 \
 *   -- mvn exec:java -D exec.mainClass=io.dapr.examples.bindings.http.OutputBindingExample
 */
public class OutputBindingExample {

  public static class MyClass {
    public MyClass() {
    }

    public String message;
  }

  static final String BINDING_NAME = "sample123";

  /**
   * The main method of this app.
   *
   * @param args Not used.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public static void main(String[] args) {
    DaprClient client = new DaprClientBuilder().build();

    int count = 0;
    while (!Thread.currentThread().isInterrupted()) {
      String message = "Message #" + (count++);

      // Randomly decides between a class type or string type to be sent.
      if (Math.random() >= 0.5) {
        // This is an example of sending data in a user-defined object.  The input binding will receive:
        //   {"message":"hello"}
        MyClass myClass = new MyClass();
        myClass.message = message;

        System.out.println("sending a class with message: " + myClass.message);
        client.invokeBinding(BINDING_NAME, myClass).block();
      } else {
        System.out.println("sending a plain string: " + message);
        client.invokeBinding(BINDING_NAME, message).block();
      }

      try {
        Thread.sleep((long) (10000 * Math.random()));
      } catch (InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }
    }

    System.out.println("Done.");
  }
}