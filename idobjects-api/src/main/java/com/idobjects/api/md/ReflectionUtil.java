package com.idobjects.api.md;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.IdObjectException;
import com.idobjects.api.ModelScope;
import com.idobjects.api.ObjectIdentifier;

public class ReflectionUtil{

    public static IdObjectMD getIdObjectMD( Class< ? extends IdObjectMD> clazz ){
        return ( IdObjectMD )getInstance( clazz );
    }

    private static Object getInstance( Class idObjectMDclazz ){
        try{
            Method instanceMethod = idObjectMDclazz.getMethod( "instance" );
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

    public static <T> List<T> cast( List list, Class<T> typeToCast ){
        return ( List<T> )list;

    }

    public static ModelMetadata getModelMetaDataInstance( Class modelMetadataClass ){
        return ( ModelMetadata )getInstance( modelMetadataClass );
    }

    public static AbstractIdObject newIdObject( Class clazz, ModelScope modelScope, ObjectIdentifier objectIdentifier ){
        try{
            Constructor constructor = clazz.getConstructor( ModelScope.class, ObjectIdentifier.class );
            return ( AbstractIdObject )constructor.newInstance( modelScope, objectIdentifier );
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
        catch( InstantiationException e ){
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
