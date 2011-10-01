package com.idobjects.api.test.company;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.ModelScope;
import com.idobjects.api.ObjectIdentifier;

public class Employee extends AbstractIdObject{
    
    public Employee( ModelScope modelScope, ObjectIdentifier objectId ){
        super( modelScope, objectId );
    }

    public String getFirstName(){
        return ( String )getPropertyValue( EmployeeMD.FIRST_NAME );
    }

    public void setFirstName( String firstName ){
        setPropertyValue( EmployeeMD.FIRST_NAME, firstName );
    }

    public void removeFirstName(){
        removePropertyValue( EmployeeMD.FIRST_NAME );
    }

    public String getLastName(){
        return ( String )getPropertyValue( EmployeeMD.LAST_NAME );
    }

    public void setLastName( String lastName ){
        setPropertyValue( EmployeeMD.LAST_NAME, lastName );
    }

    public void setDepartment( Department department ){
        addReference( EmployeeMD.DEPARTMENT, department );
    }

    public Department getDepartment(){
        return ( Department )getSingleReference( EmployeeMD.DEPARTMENT );
    }

    public void removeDepartment(){
        removeReference( EmployeeMD.DEPARTMENT );
    }

}
