package com.idobjects.persistence.api;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ModelScopeKeyPO{

    private Integer id;
    private String modelScopeKey;

    @Id
    @GeneratedValue
    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    public String getModelScopeKey(){
        return modelScopeKey;
    }

    public void setModelScopeKey( String modelScopeKey ){
        this.modelScopeKey = modelScopeKey;
    }

}
