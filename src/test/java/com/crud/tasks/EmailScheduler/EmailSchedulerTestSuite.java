package com.crud.tasks.EmailScheduler;

import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.scheduler.EmailScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailSchedulerTestSuite {

    @InjectMocks
    private EmailScheduler emailScheduler;

    @MockBean
    private TaskRepository taskRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void shouldMessageBuilder() {
        //Given

        //When
        //when(taskRepository.count()).thenReturn(5L);

        //emailScheduler.sendInformationEmail();

        //Then


    }


}
