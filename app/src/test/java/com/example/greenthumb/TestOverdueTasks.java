package com.example.greenthumb;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestOverdueTasks {
    @Test
    public void taskOverdueTest() {
        Task task = new Task("fdfd-fdfd", "test title", new Date(3333), null);

        // should be overdue
        assertEquals(task.isOverdue, true);

        // set task due date into future
        task = new Task("test", "test", new Date(100000000), null);
        // now the task shouldn't be overdue
        assertEquals(task.isOverdue, false);
    }
}
