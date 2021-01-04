package com.example.adiworkshop.mapper;

import com.example.adiworkshop.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CRUDMapper {
    /**
     * MyBatis: Useful when query statements are simple and short
     * @return Matching rows of the query, wrapped into a List
     */
    @Select("SELECT * from Users")
    List<User> getAllUsers();

    /**
     * If you want a String to have "" in MySQL query, use #{}
     * If you want a String not to have "" in MySQL query, use ${}
     * For example, the below query would be equivalent of:
     *     SELECT userEmail from Users where userName="some name";
     * @param username
     * @return
     */
    @Select("SELECT userEmail from Users where userName=#{username}")
    String getUserEmail(String username);

    @Insert("INSERT INTO Users (userName, userEmail, userRole) values " +
            "(#{userName}, #{userEmail}, #{userRole})")
    Integer createNewUser(User user);

    /**
     * Notice the user of ${user.userId} instead of #{user.userId}
     * @param user DTO containing the new user data
     * @return number of affected rows
     */
    @Update("UPDATE Users set userName=#{userName}, userEmail=#{userEmail}, userRole=#{userRole} " +
            "WHERE userId=${userId}")
    Integer updateUser(User user);

    @Delete("DELETE FROM Users WHERE userId=${userId}")
    Integer deleteUser(Integer userId);
}
