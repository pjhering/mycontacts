package mycontacts.data;

import java.util.Date;
import java.util.List;

public interface DAO
{

    public void shutdown() throws DAOException;

    public boolean addMemberToGroup(Person member, Group group) throws DAOException;

    public Email createEmail(String value, Person person) throws DAOException;

    public Group createGroup(String name) throws DAOException;

    public Person createPerson(String fname, String lname, Date bdate) throws DAOException;

    public Phone createPhone(String value, Person person) throws DAOException;

    public boolean deleteEmail(Email email) throws DAOException;

    public boolean deleteGroup(Group group) throws DAOException;

    public boolean deletePerson(Person person) throws DAOException;

    public boolean deletePhone(Phone phone) throws DAOException;

    public List<Group> findAllGroups() throws DAOException;

    public List<Person> findAllPeople() throws DAOException;

    public List<Email> findEmailsFor(Person person) throws DAOException;

    public List<Person> findMembersFor(Group group) throws DAOException;

    public List<Phone> findPhonesFor(Person person) throws DAOException;

    public boolean removeMemberFromGroup(Person memeber, Group group) throws DAOException;

    public boolean updateEmail(Email email) throws DAOException;

    public boolean updateGroup(Group group) throws DAOException;

    public boolean updatePerson(Person phone) throws DAOException;

    public boolean updatePhone(Phone phone) throws DAOException;
}
