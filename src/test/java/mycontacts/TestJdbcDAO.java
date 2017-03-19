package mycontacts;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestJdbcDAO
{
    public static void main(String[] args) throws Exception
    {
        JdbcDAO dao = new JdbcDAO("org.h2.Driver", "jdbc:h2:~/mycontacts/h2", "mycontacts", "mycontacts");
        dao.executeScript(dao.loadScript("/drop-h2.ddl"));
        dao.executeScript(dao.loadScript("/create-h2.ddl"));
        dao.loadStatements("/statements-h2.properties");
        List<Person> people = createSomePeople(dao);
        addSomePhones(dao, people);
        addSomeEmails(dao, people);
        
        List<Person> all = dao.findAllPeople();
        out.println(all);
        
        dao.shutdown();
    }

    private static List<Person> createSomePeople(JdbcDAO dao) throws DAOException
    {
        Calendar cal = Calendar.getInstance();
        List<Person> list = new ArrayList<>();
        
        cal.set(1978, 5, 21);
        list.add(dao.createPerson("John", "Doe", cal.getTime()));
        
        cal.set(1990, 3, 18);
        list.add(dao.createPerson("Jane", "Doe", cal.getTime()));
        
        cal.set(1990, 3, 19);
        list.add(dao.createPerson("Jane", "Doe", cal.getTime()));
        
        list.add(dao.createPerson("Jane", "Doe", null));
        
        return list;
    }
    
    private static void addSomePhones(JdbcDAO dao, List<Person> people) throws DAOException
    {
    }
    
    private static void addSomeEmails(JdbcDAO dao, List<Person> people) throws DAOException
    {
    }
}
