package com.example.ChatApp.Repository;
import com.example.ChatApp.Entity.Message;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("SELECT m FROM Message m WHERE " +
                  "(m.sender = :user1 AND m.recepient = :user2) OR " +
                  "(m.sender = :user2 AND m.recepient = :user1) " +
                  "ORDER BY m.stampTime ASC")
List<Message> findPrivateMessagesBetweenTwoUsers(@Param("user1") String user1, @Param("user2") String user2);
}
