package com.crud.tasks.repository;

import com.crud.tasks.domain.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Override
    List<Task> findAll();

    List<Task> findAllByTitleContains(String title);

    @Override
    Task save(Task task);

    void removeById(Long id);

    @Query(value = "Select * from tasks where id like :PARAM", nativeQuery = true)
    List<Task> findAllById(@Param("PARAM") String taskId);

    @Override
    Optional<Task> findById(Long id);
}
