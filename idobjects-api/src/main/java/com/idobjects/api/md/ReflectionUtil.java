package com.idobjects.api.md;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.idobjects.api.IdObjectException;

public class ReflectionUtil{

    public static IdObjectMD getIdObjectMD( Class< ? extends IdObjectMD> clazz ){
        try{
            Method instanceMethod = clazz.getMethod( "instance" );
            return ( IdObjectMD )instanceMethod.invoke( null );
        }
        catch( SecurityException e ){
            throw new IdObjectException( e );
        }
        catch( NoSuchMethodException e ){
            throw new IdObjectException( e );
        }

        catch( IllegalArgumentException e ){
            throw new IdObjectException( e );
        }
        catch( IllegalAccessException e ){
            throw new IdObjectException( e );
        }
        catch( InvocationTargetException e ){
            throw new IdObjectException( e );
        }
    }

}
