package com.phyous.logger.exception;


import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: pyoussef
 * Date: 10/8/12
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpNotFoundException extends HttpErrorException {
  public HttpNotFoundException(String message){
      super(message);
    }

  public HttpResponseStatus status(){
    return HttpResponseStatus.NOT_FOUND;
  }

}
