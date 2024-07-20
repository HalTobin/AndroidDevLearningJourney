package com.cleanup.todoc.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Comparator;

public class TaskWithProject {

    @Embedded
    public Task task;

    @Relation(parentColumn = "projectId", entityColumn = "id")
    public Project project;

    public Task getTask() { return task; }

    public Project getProject() { return project; }

    public void setTask(Task task) { this.task = task; }

    public void setProject(Project project) { this.project = project; }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskProjectComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return left.project.getName().compareTo(right.project.getName());
        }
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return left.task.getTaskName().compareTo(right.task.getTaskName());
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return right.task.getTaskName().compareTo(left.task.getTaskName());
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return (int) (right.task.getCreationTimestamp() - left.task.getCreationTimestamp());
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<TaskWithProject> {
        @Override
        public int compare(TaskWithProject left, TaskWithProject right) {
            return (int) (left.task.getCreationTimestamp() - right.task.getCreationTimestamp());
        }
    }

}
