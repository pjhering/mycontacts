package mycontacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
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

public class JdbcDAO
{

    private final Connection JDBC;
    private final Map<String, PreparedStatement> STMTS;

    public JdbcDAO(String driverClassName, String dbUrl, String user, String pass) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        Class driverClass = Class.forName(driverClassName);
        Driver driver = (Driver) driverClass.newInstance();
        DriverManager.registerDriver(driver);
        JDBC = DriverManager.getConnection(dbUrl, user, pass);
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

    private PreparedStatement prepare(String key) throws SQLException
    {
        PreparedStatement ps = STMTS.get(key);
        ps.clearParameters();
        return ps;
    }

    public Person createPerson(String fname, String lname, Date bdate) throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("person.create");
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
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next())
                {
                    long id = rs.getLong(1);

                    return new Person(id, fname, lname, bdate);
                }

                rs.close();
            }

            return null;
        }
        catch (Exception ex)
        {
            throw new DAOException(ex);
        }
    }

    public List<Person> findAllPeople() throws DAOException
    {
        try
        {
            PreparedStatement ps = prepare("person.findAll");
            return people(ps);
        }
        catch (SQLException ex)
        {
            throw new DAOException(ex);
        }
    }

    private List<Person> people(PreparedStatement ps) throws SQLException
    {
        ResultSet rs = ps.executeQuery();
        List<Person> list = new ArrayList<>();

        while (rs.next())
        {
            list.add(person(rs));
        }

        rs.close();

        return list;
    }

    private Person person(ResultSet rs) throws SQLException
    {
        long id = rs.getLong("id");
        String fname = rs.getString("firstname");
        String lname = rs.getString("lastname");
        Date bdate = rs.getDate("birthdate");
        
        return new Person(id, fname, lname, bdate);
    }
}
