package dao;

//import com.dkit.oop.sd2.DTOs.User;
//import com.dkit.oop.sd2.Exceptions.DaoException;
import java.util.List;

public interface UserDaoInterface
{
    public List<User> findAllUsers() throws DaoException;

    public User findUserByUsernamePassword(String username, String password) throws DaoException;

    public void insertUser(User user) throws DaoException;
}

