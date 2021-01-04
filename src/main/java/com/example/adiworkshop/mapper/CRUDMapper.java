package com.example.adiworkshop.mapper;

import com.example.adiworkshop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CRUDMapper {
    /**
     * MyBatis: Useful when query statements are simple and short
     * @return Matching rows of the query, wrapped into a List
     */
    @Select("SELECT * from Users")
    List<User> getAllUsers();
}
