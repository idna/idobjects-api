package com.idobjects.persistence.api;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class ModelScopeVersionPO{

    private Integer id;
    private ModelScopeKeyPO modelScopeKey;
    private int version;

    @Id
    @GeneratedValue
    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    public int getVersion(){
        return version;
    }

    public void setVersion( int version ){
        this.version = version;
    }

    @Transient
    public ModelScopeKeyPO getModelScopeKey(){
        return modelScopeKey;
    }

    public void setModelScopeKey( ModelScopeKeyPO modelScopeKey ){
        this.modelScopeKey = modelScopeKey;
    }

}
