package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTestSuite {

    @Autowired
    private TrelloMapper trelloMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Test
    public void testTrelloBoardMapper() {
        //Given
        TrelloListDto trelloDto = new TrelloListDto("55", "Test trello list 55", false);
        TrelloListDto trelloDto1 = new TrelloListDto("66", "Test trello list 66", true);
        List<TrelloListDto> trelloListDto = new ArrayList<>();
        trelloListDto.add(trelloDto);
        trelloListDto.add(trelloDto1);
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("1", "Trello board 1", trelloListDto);
        List<TrelloBoardDto> trelloBoardDtoList = new ArrayList<>();
        trelloBoardDtoList.add(trelloBoardDto);

        //When
        List<TrelloList> trelloList = trelloMapper.mapToList(trelloListDto);
        List<TrelloBoard> trelloBoardList = trelloMapper.mapToBoards(trelloBoardDtoList);
        List<TrelloListDto> newTrelloListDto = trelloMapper.mapToListDto(trelloList);
        int trelloListCount = trelloList.size();
        int trelloBoardsCount = trelloBoardList.size();
        String firstTrelloListName = trelloList.get(0).getName();
        String trelloFirstBoardsId = trelloBoardList.get(0).getId();
        Boolean isTaskClosed = newTrelloListDto.get(0).isClosed();

        //Then
        Assert.assertEquals(2, trelloListCount);
        Assert.assertEquals(1, trelloBoardsCount);
        Assert.assertEquals("Test trello list 55", firstTrelloListName);
        Assert.assertEquals("1", trelloFirstBoardsId);
        Assert.assertFalse(isTaskClosed);
    }

    @Test
    public void testTrelloCardMapper() {
        //Given
        TrelloCard trelloCard = new TrelloCard("New task assigment", "Build new CRUD application", "top", "56345584");

        //When
        TrelloCardDto trelloCardDto = trelloMapper.mapToCardDto(trelloCard);
        TrelloCard newTrelloCard = trelloMapper.mapToCard(trelloCardDto);
        String nameCard = trelloCardDto.getName();
        String listIdCard = trelloCardDto.getListId();
        String descCard = newTrelloCard.getDescription();

        //Then
        Assert.assertEquals("New task assigment", nameCard);
        Assert.assertEquals("Build new CRUD application", descCard);
        Assert.assertEquals("56345584", listIdCard);
    }

    @Test
    public void testTaskMapper() {
        //Given
        Task task = new Task(1L, "New test task title", "New test task content" );
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);

        //When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);
        List<TaskDto> taskDtoList = taskMapper.mapToTaskDtoList(taskList);
        Task newTask = taskMapper.mapToTask(taskDto);

        //Then
        Assert.assertEquals(new Long(1), taskDto.getId());
        Assert.assertEquals("New test task title", taskDto.getTitle());
        Assert.assertEquals("New test task content", taskDto.getContent());

        Assert.assertEquals(taskDto.getId(),newTask.getId());
        Assert.assertEquals(taskDto.getTitle(),newTask.getTitle());
        Assert.assertEquals(taskDto.getContent(),newTask.getContent());

        Assert.assertEquals(1,taskDtoList.size());
        Assert.assertEquals("New test task title",taskDtoList.get(0).getTitle());
    }

}
