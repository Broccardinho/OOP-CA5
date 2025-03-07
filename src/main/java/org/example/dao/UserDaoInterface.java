package org.example.dao;

import org.example.dto.MonzaPerformanceDTO;
import org.example.Exceptions.DaoException;


import java.util.List;

public interface UserDaoInterface {
    List<MonzaPerformanceDTO> getAllRacers() throws DaoException;
}
