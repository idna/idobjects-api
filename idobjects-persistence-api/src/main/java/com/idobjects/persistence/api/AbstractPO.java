package com.idobjects.persistence.api;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractPO{

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
        return null;
    }

    protected void setPropertyValue( PersistencePropertyMD propertMD, Object value ){

    }
}
