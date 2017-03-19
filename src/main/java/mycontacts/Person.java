package mycontacts;

import java.util.Date;

public class Person implements Comparable<Person>
{

    private final long id;
    private String firstName;
    private String lastName;
    private Date birthdate;

    Person(long id, String fname, String lname, Date bdate)
    {
        this.id = id;
        this.firstName = fname;
        this.lastName = lname;
        this.birthdate = bdate;
    }

    public long getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Date getBirthdate()
    {
        return birthdate;
    }

    public void setBirthdate(Date birthdate)
    {
        this.birthdate = birthdate;
    }
    
    @Override
    public String toString()
    {
        return firstName + " " + lastName + ((birthdate != null) ? " " + birthdate.toString() : "");
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Person other = (Person) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public int compareTo(Person o)
    {
        int v0 = lastName.compareTo(o.lastName);
        
        if(v0 == 0)
        {
            int v1 = firstName.compareTo(o.firstName);
            
            if(v1 == 0 && birthdate != null)
            {
                return birthdate.compareTo(o.birthdate);
            }
            
            return v1;
        }
        
        return v0;
    }
}
