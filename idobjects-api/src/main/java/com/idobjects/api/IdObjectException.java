package com.idobjects.api;


public class IdObjectException extends RuntimeException{

    public IdObjectException(){
        super();
    }

    public IdObjectException( String message, Throwable cause ){
        super( message, cause );
    }

    public IdObjectException( String message ){
        super( message );
    }

    public IdObjectException( Throwable cause ){
        super( cause );
    }
    
    

}
