package com.idobjects.api.test.company;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.ModelScope;
import com.idobjects.api.ObjectIdentifier;

public class Department extends AbstractIdObject{

    public Department( ModelScope modelScope, ObjectIdentifier objectId ){
        super( modelScope, objectId );
    }

    public Employee getChief(){
        return ( Employee )getReference( DepartmentMD.CHIEF );
    }

    public void setChief( Employee chief ){
        setReference( DepartmentMD.CHIEF, chief );
    }

}
