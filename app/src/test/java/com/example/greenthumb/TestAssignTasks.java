package com.example.greenthumb;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestAssignTasks {
    @Test
    public void assignTaskTest() {
        Task task = new Task("fdfd-fdfd", TaskTitle.None, new Date(3333), null);
        // assert the task doesn't have an assignee yet
        assertNull(task.getAssigneeId());
        assertNull(task.getAssigneeLabel());
        // assign a user
        task.setAssignee(new User("fsf3-34343f-4343f", "email@email.com"));
        // check that the values are updated
        assertEquals("email@email.com", task.getAssigneeLabel());
        assertEquals("fsf3-34343f-4343f", task.getAssigneeId());
    }
}