package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class DbService {

    @Autowired
    private TaskRepository repository;

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public List<Task> getAllByTitle(String title) {
        return repository.findAllByTitleContains(title);
    }

    public Optional<Task> getTask(final Long id) {
        return repository.findById(id);
    }

    public void deleteTask(final Long id) {
        repository.removeById(id);
    }

    public List<Task> findAllById(String taskId) {
        return repository.findAllById(taskId);
    }

    public Task saveTask(final Task task) {
        return repository.save(task);
    }
}
