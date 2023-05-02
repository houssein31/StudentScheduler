package com.example.studentschedualerc868.db.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studentschedualerc868.db.Entities.Course;
import com.example.studentschedualerc868.db.Entities.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert
    void insertTerm(Term... term);
//
//    @Query("SELECT * FROM term WHERE user_name = :username")
//    List<Term> getTermsByUsername(String username);


    @Query("SELECT term_ID FROM term WHERE term_title = :title")
    int getTermIDByTitle(String title);

    @Query("SELECT * FROM term WHERE term_ID = :termID")
    Term getTermByID(int termID);

    @Query("SELECT term_title FROM term WHERE term_ID = :termID")
    String getTermTitleByTermID(int termID);

    @Query("SELECT term_title FROM term")
    List<String> getAllTermTitles();

//

    @Query("DELETE FROM term WHERE term_id = :termID")
    void delete(int termID);

    @Query("SELECT COUNT(*) > 0 AS has_courses FROM course WHERE term_ID = :termID")
    boolean hasCourse(int termID);

//    @Query("SELECT * FROM term WHERE user_name = :username")
//    List<Course> getTermsForUsername(String username);

    @Query("UPDATE term SET term_title = :termTitle, term_start_date = :termStartDate, term_end_date = :termEndDate WHERE term_id = :id")
    void updateTermByID(int id, String termTitle, String termStartDate, String termEndDate);

    @Query("SELECT * FROM term WHERE user_name = :username")
    List<Term> getTermsByUsername(String username);

    @Query("SELECT term_ID FROM term WHERE user_name = :username")
    List<Integer> getTermIdsByUsername(String username);

    @Query("SELECT * FROM term WHERE term_title LIKE '%' || :title || '%' AND user_name = :userName")
    List<Term> searchTermsByTitleAndUserName(String title, String userName);


//    @Query("SELECT term_ID FROM term WHERE user_name = :username")
//    List<Integer> getTermIdsByUsername(String username);
}
