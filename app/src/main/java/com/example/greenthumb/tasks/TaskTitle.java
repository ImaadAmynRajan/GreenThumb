package com.example.greenthumb.tasks;

import androidx.databinding.InverseMethod;

/**
 * Enum representing the different types of gardening tasks.
 * Code based on Province.java from Assignment 4.
 */
public enum TaskTitle {
    None, PlantSeasonal, Fertilize, Weed, TrimBushes, MaintainEquipment, MonitorHealth, RemovePests, ClearDebris;

    @InverseMethod("intToTitle")
    public static int titleToInt (TaskTitle title) { return (title == null) ? -1 : title.ordinal(); }
    public static TaskTitle intToTitle (int titleIdx) { return (titleIdx == -1) ? null : TaskTitle.values()[titleIdx]; }

}
