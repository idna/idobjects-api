package com.idobjects.persistence.api;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractPO{

    private static final Map<Class, Object> primitiveDefaultValues = new LinkedHashMap<Class, Object>();

    static{
        primitiveDefaultValues.put( byte.class, ( byte )0 );
        primitiveDefaultValues.put( short.class, ( short )0 );
        primitiveDefaultValues.put( int.class, ( int )0 );
        primitiveDefaultValues.put( long.class, 0L );
        primitiveDefaultValues.put( float.class, 0.0f );
        primitiveDefaultValues.put( double.class, 0.0d );
        primitiveDefaultValues.put( boolean.class, false );
        primitiveDefaultValues.put( char.class, '\u0000' );

    }

    private Map<PersistencePropertyMD, Object> propertyValues = new LinkedHashMap<PersistencePropertyMD, Object>();

    private Integer id;
    private String objectId;

    @Id
    @GeneratedValue
    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    @Column(nullable = false)
    public String getObjectId(){
        return objectId;
    }

    public void setObjectId( String id ){
        this.objectId = id;
    }

    protected Object getPropertyValue( PersistencePropertyMD propertyMD ){
        Object result = propertyValues.get( propertyMD );
        if( result != null || !propertyMD.getType().isPrimitive() ) return result;
        return primitiveDefaultValues.get( propertyMD.getType() );
    }

    protected void setPropertyValue( PersistencePropertyMD propertMD, Object value ){
        propertyValues.put( propertMD, value );
    }
}
