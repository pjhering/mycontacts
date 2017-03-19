package mycontacts;

import java.util.Date;
import java.util.List;

public interface DAO
{
    public void shutdown() throws DAOException;
    
    public Email createEmail(String value, Person person) throws DAOException;
    
    public boolean deleteEmail(Email email) throws DAOException;
    
    public List<Email> findEmailsFor(Person person) throws DAOException;
    
    public boolean updateEmail(Email email) throws DAOException;
    
    public boolean addGroupMember(Group group, Person member) throws DAOException;
    
    public Group createGroup(String name) throws DAOException;
    
    public boolean deleteGroup(Group group) throws DAOException;
    
    public List<Person> findGroupMembers(Group group) throws DAOException;
    
    public boolean removeGroupMember(Group group, Person member) throws DAOException;
    
    public boolean updateGroup(Group group) throws DAOException;
    
    public Person createPerson(String fname, String lname, Date bdate) throws DAOException;
    
    public boolean deletePerson(Person person) throws DAOException;
    
    public List<Person> findAllPeople() throws DAOException;
    
    public boolean updatePerson(Person person) throws DAOException;
    
    public Phone createPhone(String value, Person person) throws DAOException;
    
    public boolean deletePhone(Phone phone) throws DAOException;
    
    public List<Phone> findPhonesFor(Person phone) throws DAOException;
    
    public boolean updatePhone(Phone phone) throws DAOException;
}
