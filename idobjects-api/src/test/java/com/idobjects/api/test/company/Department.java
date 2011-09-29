package com.idobjects.api.test.company;

import java.util.List;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.ModelScope;
import com.idobjects.api.ObjectIdentifier;

public class Department extends AbstractIdObject{

    public Department( ModelScope modelScope, ObjectIdentifier objectId ){
        super( modelScope, objectId );
    }

    public Employee getChief(){
        return ( Employee )getReferencedObject( DepartmentMD.CHIEF );
    }

    public void setChief( Employee chief ){
        addReference( DepartmentMD.CHIEF, chief );
    }

    public void removeChief(){
        removeReference( DepartmentMD.CHIEF);
    }

    public void addMember( Employee employee ){
        addReference( DepartmentMD.MEMBERS, employee );
    }

    public List<Employee> getMembers(){
        return getCastedReferences( DepartmentMD.MEMBERS, Employee.class );
    }

}
