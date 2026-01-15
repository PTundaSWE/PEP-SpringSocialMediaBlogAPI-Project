package org.paul.springbootlearning.repository;

import org.paul.springbootlearning.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByPostedBy(Integer postedBy);
}
