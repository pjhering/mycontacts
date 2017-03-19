package mycontacts.data;

public class Phone implements Comparable<Phone>
{

    private final long id;
    private String value;
    private Person person;

    public Phone(long id, String value, Person person)
    {
        this.id = id;
        this.value = value;
        this.person = person;
    }

    public long getId()
    {
        return id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Person getPerson()
    {
        return person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }

    @Override
    public String toString()
    {
        return value;
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
        final Phone other = (Phone) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public int compareTo(Phone o)
    {
        return value.compareTo(o.value);
    }
}
