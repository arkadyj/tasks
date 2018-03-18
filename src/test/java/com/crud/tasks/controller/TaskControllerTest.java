package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    private TaskNotFoundException taskNotFoundException;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldGetEmptyTaskList() throws Exception {
        //Given
        List<TaskDto> taskListDto = new ArrayList<>();
        when(taskMapper.mapToTaskDtoList(dbService.getAllTasks())).thenReturn(taskListDto);

        //When & Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetTaskList() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test", "Test content");
        List<TaskDto> taskListDto = new ArrayList<>();
        taskListDto.add(taskDto);

        when(taskMapper.mapToTaskDtoList(dbService.getAllTasks())).thenReturn(taskListDto);

        //When & Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test")))
                .andExpect(jsonPath("$[0].content", is("Test content")));
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test", "Test content");
        TaskDto updatedtaskDto = new TaskDto(1L, "Updated title", "Updated content");
        when(taskMapper.mapToTaskDto(dbService.saveTask(taskMapper.mapToTask(taskDto)))).thenReturn(updatedtaskDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(updatedtaskDto);

        //When & Then
        mockMvc.perform(put("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated title")))
                .andExpect(jsonPath("$.content", is("Updated content")));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test", "Test content");
        Long taskId = 1L;
        doNothing().when(dbService).deleteTask(1L);

        mockMvc.perform(delete("/v1/tasks/1")
                .param("taskId", "1"))
                .andExpect(status().isOk());
        verify(dbService, times(1)).deleteTask(1L);
        verifyNoMoreInteractions(dbService);
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test", "Test content");
        Task task = new Task(1L, "Test", "Test content");

        when(dbService.saveTask(taskMapper.mapToTask(taskDto))).thenReturn(task);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test")))
                .andExpect(jsonPath("$.content", is("Test content")));
    }

    @Test
    public void shouldGetTaskQuery() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test task query", "Content task query");
        List<TaskDto> taskListDto = new ArrayList<>();
        taskListDto.add(taskDto);
        String taskId = "";

        when(taskMapper.mapToTaskDtoList(dbService.findAllById(taskId))).thenReturn(taskListDto);

        //When & Then
        mockMvc.perform(get("/v1/tasksQuery").contentType(MediaType.APPLICATION_JSON)
                .param("taskId", "1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test task query")))
                .andExpect(jsonPath("$[0].content", is("Content task query")));
    }

    @Test
    public void shouldGetTaskByTitle() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Test task title", "Content task title");
        List<TaskDto> taskListDto = new ArrayList<>();
        taskListDto.add(taskDto);
        String title = "Test task title";

        when(taskMapper.mapToTaskDtoList(dbService.getAllByTitle(title))).thenReturn(taskListDto);

        //When & Then
        mockMvc.perform(get("/v1/tasksTitle").contentType(MediaType.APPLICATION_JSON)
                .param("title", "Test task title"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test task title")))
                .andExpect(jsonPath("$[0].content", is("Content task title")));
    }

    @Test
    public void shouldGetTask() throws Exception {
        //Given
        Optional<Task> task = Optional.of(new Task(333L, "Test task", "Content task"));
        TaskDto taskDto = new TaskDto(333L, "Test task", "Content task");

        when(dbService.getTask(anyLong())).thenReturn(task);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(taskDto);

        // When & Then
        String id = "{333}";
        mockMvc.perform(get("/v1/tasks/333").contentType(MediaType.APPLICATION_JSON)
                .param("taskId","333")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(333)))
                .andExpect(jsonPath("$.title", is("Test task")))
                .andExpect(jsonPath("$.content", is("Content task")));
    }
}