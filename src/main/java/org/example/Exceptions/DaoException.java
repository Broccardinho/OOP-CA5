package org.example.Exceptions;

import java.sql.SQLException;

public class DaoException extends SQLException
{
    public DaoException()
    {
        // not used
    }

    public DaoException(String aMessage)
    {
        super(aMessage);
    }

    public DaoException(String s, SQLException e) {
    }

    public DaoException(String s, ClassNotFoundException e) {
    }
}

