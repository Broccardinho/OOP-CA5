package org.example.dao;

import org.example.dto.monzaPerformanceDTO;
import org.example.Exceptions.DaoException;


import java.util.List;

public interface UserDaoInterface {
    List<monzaPerformanceDTO> getAllRacers() throws DaoException;
}
