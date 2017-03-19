package mycontacts.data;

public class DAOException extends Exception
{

    public DAOException()
    {
    }

    public DAOException(String msg)
    {
        super(msg);
    }

    public DAOException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public DAOException(String msg, Throwable cause, boolean suppress, boolean writable)
    {
        super(msg, cause, suppress, writable);
    }

    public DAOException(Throwable cause)
    {
        super(cause);
    }
}
