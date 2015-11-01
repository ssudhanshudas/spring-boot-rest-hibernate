package com.jedify.employee.service;

import com.jedify.employee.exception.NoMatchingRecordFoundException;
import com.jedify.employee.model.Employee;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j1013575 on 10/31/2015.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    List<Employee> employees;

    @Autowired
    HibernateTemplate  hibernateTemplate;

    @PostConstruct
    public void init(){
        employees = new ArrayList<>();
        employees.add(new Employee("Sudhanshu Das", "EPM123", "Hyderabad", "JDA Software"));
        employees.add(new Employee("Amit Gupta", "EPM124", "Hyderabad", "JDA Software"));
    }
    @Override
    public List<Employee> lisEmployees() {
        List<Employee>  employees = new ArrayList<>();
        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(Employee.class);
        employees = criteria.list();
        return employees;
    }

    @Override
    public Employee getEmployeeById(String id) throws NoMatchingRecordFoundException {
        for (Employee employee: employees){
            if(employee.getId().equalsIgnoreCase(id))
                return employee;
        }
        throw new NoMatchingRecordFoundException("Record Not found for the id : " + id);
    }
}
