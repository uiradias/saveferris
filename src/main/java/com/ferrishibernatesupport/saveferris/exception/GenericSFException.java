package com.ferrishibernatesupport.saveferris.exception;

/**
 * @author Fernando Nogueira
 * @since 8/30/14 6:01 PM
 */
public class GenericSFException extends RuntimeException {

    public GenericSFException(){
        super();
    }

    public GenericSFException(String message){
        super(message);
    }
}
