package com.phyous.logger.exception;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: pyoussef
 * Date: 10/8/12
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpErrorException extends Exception {
  public HttpErrorException(String message){
    super(message);
  }

  public HttpResponseStatus status(){
    return HttpResponseStatus.INTERNAL_SERVER_ERROR;
  }
}
