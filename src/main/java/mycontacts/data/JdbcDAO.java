package mycontacts.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Class.forName;
import static java.lang.System.out;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import static java.sql.DriverManager.getConnection;
import static java.sql.DriverManager.registerDriver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.DATE;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JdbcDAO implements DAO
{

    private final Connection JDBC;
    private final Map<String, PreparedStatement> STMTS;

    public JdbcDAO(String driverClassName, String dbUrl, String user, String pass) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        Class driverClass = forName(driverClassName);
        Driver driver = (Driver) driverClass.newInstance();
        registerDriver(driver);
        JDBC = getConnection(dbUrl, user, pass);
        STMTS = new HashMap<>();
    }

    public void loadStatements(String path) throws IOException, SQLException
    {
        out.println(path);
        URL url = getClass().getResource(path);
        out.println(url);
        Properties props = new Properties();
        props.load(url.openStream());

        for (String key : props.stringPropertyNames())
        {
            out.println(key);
            out.println(props.get(key));
            STMTS.put(key, JDBC.prepareStatement(props.getProperty(key)));
            out.println(STMTS.get(key) != null);
        }
    }

    public void executeScript(List<String> statements)
    {
        for (String sql : statements)
        {
            try (Statement s = JDBC.createStatement())
            {
                out.println(sql);
                boolean result = s.execute(sql);
                out.println(result);
            }
            catch (SQLException ex)
            {
                ex.printStackTrace(out);
            }
        }
    }

    public List<String> loadScript(String path)
    {
        URL url = getClass().getResource(path);
        List<String> list = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(url.openStream()))
        {
            BufferedReader buffer = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = buffer.readLine()) != null)
            {
                line = line.trim();
                builder.append(line).append('\n');

                if (line.endsWith(";"))
                {
                    list.add(builder.toString());
                    builder = new StringBuilder();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return list;
    }

    private PreparedStatement prepare(String key) throws SQLException
    {
        PreparedStatement ps = STMTS.get(key);
        ps.clearParameters();
        return ps;
    }

    private long id(PreparedStatement ps) throws SQLException
    {
        try (ResultSet rs = ps.getGeneratedKeys())
        {
            rs.next();
            long id = rs.getLong(1);

            return id;
        }
    }

    private List<Email> emails(Person person, PreparedStatement ps) throws SQLException
    {
        try (ResultSet rs = ps.executeQuery())
        {
            List<Email> list = new ArrayList<>();

            while (rs.next())
            {
                Email e = email(person, rs);
                list.add(e);
            }

            return list;
        }
    }

    private Email email(Person person, ResultSet rs) throws SQLException
    {
        long id = rs.getLong("id");
        String value = rs.getString("value");
        Email e = new Email(id, value, person);
        return e;
    }

    private List<Phone> phones(Person person, PreparedStatement ps) throws SQLException
    {
        try (ResultSet rs = ps.executeQuery())
        {
            List<Phone> list = new ArrayList<>();

            while (rs.next())
            {
                Phone p = phone(person, rs);
                list.add(p);
            }

            return list;
        }
    }

    private Phone phone(Person person, ResultSet rs) throws SQLException
    {
        long id = rs.getLong("id");
        String value = rs.getString("value");
        Phone p = new Phone(id, value, person);
        return p;
    }

    private List<Group> groups(PreparedStatement ps) throws SQLException
    {
        try (ResultSet rs = ps.executeQuery())
        {
            List<Group> list = new ArrayList<>();

            while (rs.next())
            {
                Group g = group(rs);
                list.add(g);
            }

            return list;
        }
    }

    private Group group(ResultSet rs) throws SQLException
    {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        Group g = new Group(id, name);
        return g;
    }

    private List<Person> people(PreparedStatement ps) throws SQLException
    {
        try (ResultSet rs = ps.executeQuery())
        {
            List<Person> list = new ArrayList<>();

            while (rs.next())
            {
                Person p = person(rs);
                list.add(person(rs));
            }

            return list;
        }
    }

    private Person person(ResultSet rs) throws SQLException
    {
        long id = rs.getLong("id");
        String fname = rs.getString("firstname");
        String lname = rs.getString("lastname");
        Date bdate = rs.getDate("birthdate");
        Person p = new Person(id, fname, lname, bdate);
        return p;
    }

    @Override
    public void shutdown() throws DAOException
    {
        try
        {
            JDBC.close();
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean addMemberToGroup(Person member, Group group) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("addMemberToGroup");
            ps.setLong(1, member.getId());
            ps.setLong(2, group.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public Email createEmail(String value, Person person) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("createEmail");
            ps.setString(1, value);
            ps.setLong(2, person.getId());
            int count = ps.executeUpdate();
            if (count == 1)
            {
                long id = id(ps);
                Email e = new Email(id, value, person);

                return e;
            }
            else
            {
                return null;
            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public Group createGroup(String name) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("createGroup");
            ps.setString(1, name);
            int count = ps.executeUpdate();
            if (count == 1)
            {
                long id = id(ps);
                Group g = new Group(id, name);

                return g;
            }
            else
            {
                return null;
            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public Person createPerson(String fname, String lname, Date bdate) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("createPerson");
            ps.setString(1, fname);
            ps.setString(2, lname);

            if (bdate != null)
            {
                ps.setDate(3, new java.sql.Date(bdate.getTime()));
            }
            else
            {
                ps.setNull(3, DATE);
            }

            int count = ps.executeUpdate();
            if (count == 1)
            {
                long id = id(ps);
                Person p = new Person(id, fname, lname, bdate);

                return p;
            }
            else
            {
                return null;
            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public Phone createPhone(String value, Person person) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("createPhone");
            ps.setString(1, value);
            ps.setLong(2, person.getId());
            int count = ps.executeUpdate();
            if (count == 1)
            {
                long id = id(ps);
                Phone p = new Phone(id, value, person);

                return p;
            }
            else
            {
                return null;
            }
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean deleteEmail(Email email) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("deleteEmail");
            ps.setLong(1, email.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean deleteGroup(Group group) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("deleteGroup");
            ps.setLong(1, group.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean deletePerson(Person person) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("deletePerson");
            ps.setLong(1, person.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean deletePhone(Phone phone) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("deletePhone");
            ps.setLong(1, phone.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Group> findAllGroups() throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("findAllGroups");
            return groups(ps);
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Person> findAllPeople() throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("findAllPeople");
            return people(ps);
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Email> findEmailsFor(Person person) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("findEmailsFor");
            ps.setLong(1, person.getId());
            return emails(person, ps);
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Person> findMembersFor(Group group) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("findMembersFor");
            ps.setLong(1, group.getId());
            return people(ps);
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public List<Phone> findPhonesFor(Person person) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("findPhonesFor");
            ps.setLong(1, person.getId());
            return phones(person, ps);
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean removeMemberFromGroup(Person member, Group group) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("removeMemberFromGroup");
            ps.setLong(1, member.getId());
            ps.setLong(2, group.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean updateEmail(Email email) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("updateEmail");
            ps.setString(1, email.getValue());
            ps.setLong(2, email.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean updateGroup(Group group) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("updateGroup");
            ps.setString(1, group.getName());
            ps.setLong(2, group.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean updatePerson(Person person) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("updatePerson");
            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());

            if (person.getBirthdate() != null)
            {
                ps.setDate(3, new java.sql.Date(person.getBirthdate().getTime()));
            }
            else
            {
                ps.setNull(3, DATE);
            }

            ps.setLong(4, person.getId());

            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    @Override
    public boolean updatePhone(Phone phone) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("updatePhone");
            ps.setString(1, phone.getValue());
            ps.setLong(2, phone.getId());
            int count = ps.executeUpdate();
            return count == 1;
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }
}
