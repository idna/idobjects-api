package com.idobjects.persistence.api;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class ReferenceVersionPO{

    private Integer id;
    private ModelScopeVersionPO version;
    private ReferencePO reference;

    @Id
    @GeneratedValue
    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    @Transient
    public ModelScopeVersionPO getVersion(){
        return version;
    }

    public void setVersion( ModelScopeVersionPO version ){
        this.version = version;
    }

    @Transient
    public ReferencePO getReference(){
        return reference;
    }

    public void setReference( ReferencePO reference ){
        this.reference = reference;
    }

}
