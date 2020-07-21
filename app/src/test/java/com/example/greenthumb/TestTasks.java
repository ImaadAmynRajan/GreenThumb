package com.example.greenthumb;

import com.example.greenthumb.tasks.Task;
import com.example.greenthumb.tasks.TaskTitle;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestTasks {
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

    // test Task constructor using the isFinished flag
    @Test
    public void testConstructor() {
        // create unfinished task
        Task unfinishedTask = new Task("fdfd-fdfd", TaskTitle.None, new Date(3333), null, false);

        // assert that task is marked as unfinished
        assertFalse(unfinishedTask.isFinished());

        // create finished task
        Task finishedTask = new Task("fdfd-fdfd", TaskTitle.None, new Date(3333), null, true);

        // assert that the task is marked as finished
        assertTrue(finishedTask.isFinished());
    }

    @Test
    public void taskOverdueTest() {
        Task task = new Task("fdfd-fdfd", TaskTitle.None, new Date(3333), null);

        // should be overdue
        assertTrue(task.isOverdue());
        // mark it as finished
        task.markAsFinished();

        // now shouldn't be overdue
        assertFalse(task.isOverdue());

        // make a new task in the future
        long curDate = Calendar.getInstance().getTimeInMillis();
        task = new Task("test", TaskTitle.None, new Date(curDate + 1000), null);

        // this task shouldn't be over due
        assertFalse(task.isOverdue());
    }

    @Test
    public void recurringTaskTest() {
        // initialize a task without a recurring task
        Task task = new Task("fdfd-fdfd", TaskTitle.None, new Date(3333), null);
        // should return -1 if it doesn't have a recurring interval
        assertTrue(task.getInterval() == -1);
        task.setInterval(3);
        // should now have an interval
        assertTrue(task.getInterval() == 3);
    }
}
