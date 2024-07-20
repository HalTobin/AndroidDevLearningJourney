package com.cleanup.todoc.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.viewModel.TaskViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor myExecutor;

    public ViewModelFactory(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor myExecutor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.myExecutor = myExecutor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(TaskViewModel.class)) {
        return (T) new TaskViewModel(taskDataSource, projectDataSource, myExecutor);
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
