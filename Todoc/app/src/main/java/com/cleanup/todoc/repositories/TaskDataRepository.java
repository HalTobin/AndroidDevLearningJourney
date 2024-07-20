package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;

import java.util.List;

public class TaskDataRepository {

    // --- INITIALIZE DAO ---
    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {this.taskDao = taskDao;}

    // --- GETTER ---
    public LiveData<Task> getTask(long taskId) {return this.taskDao.getTask(taskId);}

    public LiveData<List<Task>> getAllTasks() {return this.taskDao.getAllTasks();}

    public LiveData<List<TaskWithProject>> getAllTasksWithProject() {return this.taskDao.getAllTasksWithProject();}

    public LiveData<List<Task>> getTasksByProject(long projectId) {return this.taskDao.getTasksByProject(projectId);}

    // --- CREATE ---
    public void createTask(Task task) {taskDao.insertTask(task);}

    // --- DELETE ---
    public void deleteTask(long taskId) {taskDao.deleteTask(taskId);}

    // --- UPDATE ---
    public void updateTask(Task task) {taskDao.updateTask(task);}

}
