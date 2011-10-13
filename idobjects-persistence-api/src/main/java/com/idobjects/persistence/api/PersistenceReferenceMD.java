package com.idobjects.persistence.api;

import com.idobjects.api.md.IdObjectReferenceMD;

public class PersistenceReferenceMD{

    private final IdObjectReferenceMD referenceMD;

    public PersistenceReferenceMD( IdObjectReferenceMD referenceMD ){
        this.referenceMD = referenceMD;
    }

    public IdObjectReferenceMD getIdObjectReferenceMD(){
        return referenceMD;
    }

}
