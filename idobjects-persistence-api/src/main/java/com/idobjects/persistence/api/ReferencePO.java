package com.idobjects.persistence.api;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ReferencePO{

    private Integer id;
    private String referenceName;
    private String sourceId;
    private String destinationId;

    @Id
    @GeneratedValue
    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    @Column(name = "ref_name")
    public String getReferenceName(){
        return referenceName;
    }

    public void setReferenceName( String referenceName ){
        this.referenceName = referenceName;
    }

    @Column(name = "source_id")
    public String getSourceId(){
        return sourceId;
    }

    public void setSourceId( String sourceId ){
        this.sourceId = sourceId;
    }

    @Column(name = "destination_id")
    public String getDestinationId(){
        return destinationId;
    }

    public void setDestinationId( String destinationId ){
        this.destinationId = destinationId;
    }
}
