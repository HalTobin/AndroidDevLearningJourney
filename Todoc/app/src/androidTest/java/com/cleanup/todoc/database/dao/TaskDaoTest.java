package com.cleanup.todoc.database.dao;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.SaveMyTaskDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private SaveMyTaskDatabase myDataBase;
    private TaskDao myTaskDao;

    private static final int TASK_ID = 1;
    private static final int PROJECT_ID = 1;
    private static final Task TASK_DEMO = new Task(TASK_ID, PROJECT_ID, "Test", 123456789);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        myDataBase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), SaveMyTaskDatabase.class).allowMainThreadQueries().build();
        myTaskDao = myDataBase.taskDao();
        //PREPOPULATE PROJECT TABLE
        myDataBase.projectDao().createProject(new Project(1, "Projet Tartampion", 0xFFEADAD1));
        myDataBase.projectDao().createProject(new Project(2, "Projet Lucidi", 0xFFB4CDBA));
        myDataBase.projectDao().createProject(new Project(3, "Projet Circus", 0xFFA3CED2));
        myDataBase.projectDao().createProject(new Project(4, "Projet Puce Muse", 0xFF00AAAA));
    }

    @After
    public void closeDb() throws Exception {
        myDataBase.close();
    }

    //Test the functions "insertTask(Task)" and "getTask(taskId)"
    @Test
    public void insertAndGetTask() throws InterruptedException {
        myTaskDao.insertTask(TASK_DEMO);
        Task task = LiveDataTestUtil.getValue(myTaskDao.getTask(1));
        assertEquals(task.getTaskName(), TASK_DEMO.getTaskName());
        assertEquals(task.getTaskId(), TASK_DEMO.getTaskId());
        assertEquals(task.getProjectId(), TASK_DEMO.getProjectId());
    }

    //Check that the Project associate with a new task is the good one
    @Test
    public void insertAndGetTaskWithProject() throws InterruptedException {
        myTaskDao.insertTask(TASK_DEMO);
        List<TaskWithProject> tasksWithProject = LiveDataTestUtil.getValue(myTaskDao.getAllTasksWithProject());
        TaskWithProject taskWithProject = tasksWithProject.get(TASK_ID - 1);

        assertEquals(taskWithProject.getTask().getTaskName(), TASK_DEMO.getTaskName());
        assertEquals(taskWithProject.getTask().getTaskId(), TASK_DEMO.getTaskId());
        assertEquals(taskWithProject.getTask().getProjectId(), TASK_DEMO.getProjectId());
        assertEquals(taskWithProject.getTask().getProjectId(), taskWithProject.getProject().getId());
    }

    //Test the functions "insertTask(Task)" and "deleteTask(taskId)"
    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        myTaskDao.insertTask(TASK_DEMO);
        Task taskAdded = LiveDataTestUtil.getValue(myTaskDao.getAllTasks()).get(0);
        myTaskDao.deleteTask(taskAdded.getTaskId());

        List<Task> tasks = LiveDataTestUtil.getValue(myTaskDao.getAllTasks());
        assertTrue(tasks.isEmpty());
    }

}
