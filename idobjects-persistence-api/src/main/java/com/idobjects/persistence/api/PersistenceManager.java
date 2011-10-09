package com.idobjects.persistence.api;

import com.idobjects.api.ModelScope;

public interface PersistenceManager{

    void saveModelScope( ModelScope modelScope );

    void saveModelSope( ModelScope modelScope, ChangeRecorder changes );

}
