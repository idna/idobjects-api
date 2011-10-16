package com.idobjects.persistence.api;

import com.idobjects.api.ModelScope;
import com.idobjects.api.ModelScopeIdentifier;

public interface PersistenceManager{

    void saveModelScope( ModelScope modelScope );

    void saveModelSope( ModelScope modelScope, ChangeRecorder changes );

    ModelScope readLatestModelScope( ModelScopeIdentifier modelScopeIdentifier );

}
