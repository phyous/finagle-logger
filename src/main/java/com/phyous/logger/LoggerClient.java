package com.phyous.logger;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.http.Http;
import com.twitter.util.FutureEventListener;
import org.jboss.netty.handler.codec.http.*;

public class LoggerClient {
  public static void main(String[] args) {
    // Create client
    Service<HttpRequest, HttpResponse> httpClient =
        ClientBuilder.safeBuild(
            ClientBuilder.get()
                .codec(Http.get())
                .hosts("localhost:8880")
                .hostConnectionLimit(1));

    // Create request
    HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");

    // Handle request asynchronously
    httpClient.apply(request).addEventListener(new FutureEventListener<HttpResponse>() {
      @Override
      public void onSuccess(HttpResponse response4) {
        System.out.println("Success!");
        try{Thread.sleep(1000);} catch (Exception e){}
        System.out.println("Success waiting too!");
      }

      @Override
      public void onFailure(Throwable throwable) {
        System.out.println("Exception thrown by client: " + throwable);
      }
    });

    // Close connection
    httpClient.release();
  }
}

