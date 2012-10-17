package com.phyous.logger;

import com.phyous.logger.exception.HttpErrorException;
import com.phyous.logger.exception.HttpNotFoundException;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.http.Http;
import com.twitter.util.Future;
import com.twitter.util.FutureTransformer;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

public class LoggerServer extends Service<HttpRequest, HttpResponse> {
  /**
   * Static main method that can be used to start server
   * @param args TBD
   */
  public static void main(String[] args) {
    PropertyConfigurator.configure("cfg/log4j/finagle-logger.properties");

    System.out.println("Starting Server...");

    ServerBuilder.safeBuild(new LoggerServer(),
        ServerBuilder.get()
            .codec(Http.get())
            .name("LoggerServer")
            .bindTo(new InetSocketAddress("localhost", 8880)));

  }

  /**
   * Encapsulates service logic
   * @param request input HttpRequest
   * @return Future for HttpResponse
   */
  public Future<HttpResponse> apply(HttpRequest request) {
    Logger.getLogger("server_log").info(request.toString());

    Future<HttpResponse> response;
    try {
      Future<String> responseContentFuture = logContentAsync(request);
      response = generateResposne(responseContentFuture);
    } catch (HttpErrorException e){
      HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, e.status());
      httpResponse.setContent(ChannelBuffers.wrappedBuffer(e.getMessage().getBytes()));
      response = Future.value(httpResponse);
    }

    return response;
  }

  private Future<HttpResponse> generateResposne(Future<String> responseContentFuture){
    return responseContentFuture.transformedBy(new FutureTransformer<String, HttpResponse>() {
          @Override
          public HttpResponse map(String content) {
            HttpResponse httpResponse =
                new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            httpResponse.setContent(ChannelBuffers.wrappedBuffer(content.getBytes()));
            return httpResponse;
          }

          @Override
          public HttpResponse handle(Throwable throwable) {
            HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE);
            httpResponse.setContent(ChannelBuffers.wrappedBuffer(throwable.toString().getBytes()));
            return httpResponse;
          }
        });
  }

  /**
    * Logs content from request to a logfile if request matches logging route
    * @param request
    * @return Future<String> corresponding to response body
    */
   private Future<String> logContentAsync(HttpRequest request) throws HttpErrorException {
    // Parse URI
    URI uri;
    try {
      uri = new URI(request.getUri());
    } catch (URISyntaxException e) {
      throw new HttpErrorException(e.getMessage());
    }

    // Ensure route match
    if (!request.getMethod().toString().equals("POST"))
      throw new HttpNotFoundException("Http method not supported");
    else if (!uri.getPath().equalsIgnoreCase("/log"))
      throw new HttpNotFoundException("Route not found.");

     // Get bytes from request
     ChannelBuffer buffer = request.getContent();
     StringBuilder sb = new StringBuilder();
     for (int i = 0; i < buffer.capacity(); i ++) {
          byte b = buffer.getByte(i);
          sb.append((char)b);
      }
     String data = sb.toString();

     // Log data here
     Logger.getLogger("message_log").info(data);

     return Future.value("Data logged");
   }
}

