package mycontacts.data;

import static java.lang.System.out;
import java.util.Date;

public class TestJdbcDAO
{

    public static void main(String[] args) throws Exception
    {
        JdbcDAO dao = new JdbcDAO("org.h2.Driver", "jdbc:h2:~/mycontacts/h2", "mycontacts", "mycontacts");
        dao.executeScript(dao.loadScript("/drop-h2.ddl"));
        dao.executeScript(dao.loadScript("/create-h2.ddl"));
        dao.loadStatements("/statements-h2.properties");

        Person p0 = dao.createPerson("John", "Doe", null);
        Phone h01 = dao.createPhone("123-456-7890", p0);
        Phone h02 = dao.createPhone("234-567-8901", p0);
        Email e01 = dao.createEmail("e01@email.com", p0);
        Email e02 = dao.createEmail("e02@email.com", p0);

        Person p1 = dao.createPerson("Jane", "Doe", new Date());
        Phone h11 = dao.createPhone("123-456-7890", p1);
        Phone h12 = dao.createPhone("234-567-8901", p1);
        Email e11 = dao.createEmail("e11@email.com", p1);
        Email e12 = dao.createEmail("e12@email.com", p1);

        Person p2 = dao.createPerson("Sylvester", "Small", null);
        Phone h21 = dao.createPhone("123-456-7890", p2);
        Phone h22 = dao.createPhone("234-567-8901", p2);
        Email e21 = dao.createEmail("e21@email.com", p2);
        Email e22 = dao.createEmail("e22@email.com", p2);

        Person p3 = dao.createPerson("Sally", "Small", new Date());
        Phone h31 = dao.createPhone("123-456-7890", p3);
        Phone h32 = dao.createPhone("234-567-8901", p3);
        Email e31 = dao.createEmail("e31@email.com", p3);
        Email e32 = dao.createEmail("e32@email.com", p3);

        Group g0 = dao.createGroup("friends");
        dao.addMemberToGroup(p0, g0);
        dao.addMemberToGroup(p1, g0);

        Group g1 = dao.createGroup("family");
        dao.addMemberToGroup(p1, g1);
        dao.addMemberToGroup(p2, g1);

        Group g2 = dao.createGroup("others");
        dao.addMemberToGroup(p2, g2);
        dao.addMemberToGroup(p3, g2);

        out.println("---------------");
        out.println(dao.findAllPeople());
        out.println(dao.findEmailsFor(p0));
        out.println(dao.findEmailsFor(p1));
        out.println(dao.findEmailsFor(p2));
        out.println(dao.findEmailsFor(p3));
        out.println(dao.findPhonesFor(p0));
        out.println(dao.findPhonesFor(p1));
        out.println(dao.findPhonesFor(p2));
        out.println(dao.findPhonesFor(p3));
        out.println(dao.findAllGroups());
        out.println(dao.findMembersFor(g0));
        out.println(dao.findMembersFor(g1));
        out.println(dao.findMembersFor(g2));

        e32.setValue("foo@bar.com");
        dao.updateEmail(e32);
        h32.setValue("987-654-3210");
        dao.updatePhone(h32);
        p1.setFirstName("XXX");
        dao.updatePerson(p1);
        g2.setName("associates");
        dao.updateGroup(g2);

        dao.removeMemberFromGroup(p0, g0);

        out.println("---------------");
        out.println(dao.findAllPeople());
        out.println(dao.findEmailsFor(p0));
        out.println(dao.findEmailsFor(p1));
        out.println(dao.findEmailsFor(p2));
        out.println(dao.findEmailsFor(p3));
        out.println(dao.findPhonesFor(p0));
        out.println(dao.findPhonesFor(p1));
        out.println(dao.findPhonesFor(p2));
        out.println(dao.findPhonesFor(p3));
        out.println(dao.findAllGroups());
        out.println(dao.findMembersFor(g0));
        out.println(dao.findMembersFor(g1));
        out.println(dao.findMembersFor(g2));

        dao.deleteEmail(e12);
        dao.deleteGroup(g0);
        dao.deletePerson(p3);
        dao.deletePhone(h02);

        out.println("---------------");
        out.println(dao.findAllPeople());
        out.println(dao.findEmailsFor(p0));
        out.println(dao.findEmailsFor(p1));
        out.println(dao.findEmailsFor(p2));
        out.println(dao.findEmailsFor(p3));
        out.println(dao.findPhonesFor(p0));
        out.println(dao.findPhonesFor(p1));
        out.println(dao.findPhonesFor(p2));
        out.println(dao.findPhonesFor(p3));
        out.println(dao.findAllGroups());
        out.println(dao.findMembersFor(g0));
        out.println(dao.findMembersFor(g1));
        out.println(dao.findMembersFor(g2));

        dao.shutdown();
    }
}
