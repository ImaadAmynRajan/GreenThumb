package com.example.greenthumb;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestMarkTaskFinished {
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
}