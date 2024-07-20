package com.cleanup.todoc.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor myExecutor;

    @Nullable
    private LiveData<Project> currentProject;

    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor myExecutor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.myExecutor = myExecutor;
    }

    public LiveData<Project> getProject(long projectId) {return this.currentProject;}

    public LiveData<List<Project>> getAllProjects() {return projectDataSource.getAllProjects();}

    public LiveData<Task> getTask(long taskId) {return taskDataSource.getTask(taskId);}

    public LiveData<List<Task>> getAllTasks() {return taskDataSource.getAllTasks();}

    public LiveData<List<TaskWithProject>> getAllTasksWithProject() {return taskDataSource.getAllTasksWithProject();}

    public LiveData<List<Task>> getTasksByProject(long projectId) {return taskDataSource.getTasksByProject(projectId);}

    public void createTask(Task task) {
        myExecutor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    public void deleteTask(long taskId) {
        myExecutor.execute(() -> {
            taskDataSource.deleteTask(taskId);
        });
    }

    public void updateTask(Task task) {
        myExecutor.execute(() -> {
            taskDataSource.updateTask(task);
        });
    }
}
