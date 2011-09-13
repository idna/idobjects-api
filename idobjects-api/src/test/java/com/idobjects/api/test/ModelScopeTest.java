package com.idobjects.api.test;

import org.junit.Assert;
import org.junit.Test;

import com.idobjects.api.GuidObjectIdentifier;
import com.idobjects.api.ModelScope;
import com.idobjects.api.StringModelScopeIdentifier;
import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.ModelMetadata;
import com.idobjects.api.test.company.Department;
import com.idobjects.api.test.company.Employee;

public class ModelScopeTest{

    @Test
    public void testModelScope(){
        ModelScope modelScope = new ModelScope( new StringModelScopeIdentifier( "CompanyModelScope" ) );
        Assert.assertEquals( 0, modelScope.size() );

        Employee employee = new Employee( modelScope, new GuidObjectIdentifier() );
        Assert.assertTrue( modelScope.containsObject( employee.getId() ) );
        Assert.assertEquals( 1, modelScope.size() );

        employee.setFirstName( "firstName" );
        Assert.assertEquals( "firstName", employee.getFirstName() );
        employee.setLastName( "lastName" );
        Assert.assertEquals( "firstName", employee.getLastName(), "lastName" );

        Department department = new Department( modelScope, new GuidObjectIdentifier() );
        Assert.assertTrue( modelScope.containsObject( department.getId() ) );

        Assert.assertEquals( 2, modelScope.size() );

        employee.setDepartment( department );
        Assert.assertEquals( department, employee.getDepartment() );

        Assert.assertEquals( 1, department.getMembers().size() );
        Assert.assertEquals( employee, department.getMembers().get( 0 ) );

        employee.removeDepartment();
        Assert.assertNull( employee.getDepartment() );

        Assert.assertEquals( 0, department.getMembers().size() );

    }

}
