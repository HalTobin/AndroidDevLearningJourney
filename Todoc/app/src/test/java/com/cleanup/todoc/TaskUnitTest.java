package com.cleanup.todoc;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.TaskWithProject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {

    List<Project> ProjectsList = getDefaultProjectList();

    @Test
    public void test_projects() {
        final TaskWithProject taskWithProject1 = newTaskWithProject(new Task(1, "task 1", new Date().getTime()));
        final TaskWithProject taskWithProject2 = newTaskWithProject(new Task(2, "task 2", new Date().getTime()));
        final TaskWithProject taskWithProject3 = newTaskWithProject(new Task(3, "task 3", new Date().getTime()));
        final TaskWithProject taskWithProject4 = newTaskWithProject(new Task(4, "task 4", new Date().getTime()));

        assertEquals("Projet Tartampion", ProjectsList.get((int) (taskWithProject1.getTask().getProjectId()-1)).getName());
        assertEquals("Projet Lucidia", ProjectsList.get((int) (taskWithProject2.getTask().getProjectId()-1)).getName());
        assertEquals("Projet Circus", ProjectsList.get((int) (taskWithProject3.getTask().getProjectId()-1)).getName());
        assertEquals("Projet Puce Muse", ProjectsList.get((int) (taskWithProject4.getTask().getProjectId()-1)).getName());
    }

    @Test
    public void test_az_comparator() {
        final TaskWithProject taskWithProject1 = newTaskWithProject(new Task (1, 1, "aaa", 123));
        final TaskWithProject taskWithProject2 = newTaskWithProject(new Task(2, 2, "zzz", 124));
        final TaskWithProject taskWithProject3 = newTaskWithProject(new Task(3, 3, "hhh", 125));

        final ArrayList<TaskWithProject> tasksWithProject = new ArrayList<>();
        tasksWithProject.add(taskWithProject1);
        tasksWithProject.add(taskWithProject2);
        tasksWithProject.add(taskWithProject3);
        Collections.sort(tasksWithProject, new TaskWithProject.TaskAZComparator());

        assertSame(tasksWithProject.get(0), taskWithProject1);
        assertSame(tasksWithProject.get(1), taskWithProject3);
        assertSame(tasksWithProject.get(2), taskWithProject2);
    }

    @Test
    public void test_za_comparator() {
        final TaskWithProject taskWithProject1 = newTaskWithProject(new Task (1, 1, "aaa", 123));
        final TaskWithProject taskWithProject2 = newTaskWithProject(new Task(2, 2, "zzz", 124));
        final TaskWithProject taskWithProject3 = newTaskWithProject(new Task(3, 3, "hhh", 125));

        final ArrayList<TaskWithProject> tasksWithProject = new ArrayList<>();
        tasksWithProject.add(taskWithProject1);
        tasksWithProject.add(taskWithProject2);
        tasksWithProject.add(taskWithProject3);
        Collections.sort(tasksWithProject, new TaskWithProject.TaskZAComparator());

        assertSame(tasksWithProject.get(0), taskWithProject2);
        assertSame(tasksWithProject.get(1), taskWithProject3);
        assertSame(tasksWithProject.get(2), taskWithProject1);
    }

    @Test
    public void test_recent_comparator() {
        final TaskWithProject taskWithProject1 = newTaskWithProject(new Task (1, 1, "aaa", 123));
        final TaskWithProject taskWithProject2 = newTaskWithProject(new Task(2, 2, "zzz", 124));
        final TaskWithProject taskWithProject3 = newTaskWithProject(new Task(3, 3, "hhh", 125));

        final ArrayList<TaskWithProject> tasksWithProject = new ArrayList<>();
        tasksWithProject.add(taskWithProject1);
        tasksWithProject.add(taskWithProject2);
        tasksWithProject.add(taskWithProject3);
        Collections.sort(tasksWithProject, new TaskWithProject.TaskRecentComparator());

        assertSame(tasksWithProject.get(0), taskWithProject3);
        assertSame(tasksWithProject.get(1), taskWithProject2);
        assertSame(tasksWithProject.get(2), taskWithProject1);
    }

    @Test
    public void test_old_comparator() {
        final TaskWithProject taskWithProject1 = newTaskWithProject(new Task (1, 1, "aaa", 123));
        final TaskWithProject taskWithProject2 = newTaskWithProject(new Task(2, 2, "zzz", 124));
        final TaskWithProject taskWithProject3 = newTaskWithProject(new Task(3, 3, "hhh", 125));

        final ArrayList<TaskWithProject> tasksWithProject = new ArrayList<>();
        tasksWithProject.add(taskWithProject1);
        tasksWithProject.add(taskWithProject2);
        tasksWithProject.add(taskWithProject3);
        Collections.sort(tasksWithProject, new TaskWithProject.TaskOldComparator());

        assertSame(tasksWithProject.get(0), taskWithProject1);
        assertSame(tasksWithProject.get(1), taskWithProject2);
        assertSame(tasksWithProject.get(2), taskWithProject3);
    }

    @Test
    public void test_project_comparator() {
        final TaskWithProject taskWithProject1 = newTaskWithProject(new Task (1, 1, "aaa", 123));
        final TaskWithProject taskWithProject2 = newTaskWithProject(new Task(2, 2, "zzz", 124));
        final TaskWithProject taskWithProject3 = newTaskWithProject(new Task(3, 3, "hhh", 125));
        final TaskWithProject taskWithProject4 = newTaskWithProject(new Task (4, 1, "bbb", 126));
        final TaskWithProject taskWithProject5 = newTaskWithProject(new Task(5, 2, "yyy", 127));
        final TaskWithProject taskWithProject6 = newTaskWithProject(new Task(6, 3, "iii", 128));

        final ArrayList<TaskWithProject> tasksWithProject = new ArrayList<>();
        tasksWithProject.add(taskWithProject1);
        tasksWithProject.add(taskWithProject2);
        tasksWithProject.add(taskWithProject3);
        tasksWithProject.add(taskWithProject4);
        tasksWithProject.add(taskWithProject5);
        tasksWithProject.add(taskWithProject6);
        Collections.sort(tasksWithProject, new TaskWithProject.TaskProjectComparator());

        assertSame(tasksWithProject.get(0), taskWithProject3);
        assertSame(tasksWithProject.get(1), taskWithProject6);
        assertSame(tasksWithProject.get(2), taskWithProject2);
        assertSame(tasksWithProject.get(3), taskWithProject5);
        assertSame(tasksWithProject.get(4), taskWithProject1);
        assertSame(tasksWithProject.get(5), taskWithProject4);
    }

    /**
     * Function to simplify the creation of an new TaskWithProject object
     *
     * @param task
     * @return a new TaskWithProject object
     */

    public TaskWithProject newTaskWithProject(Task task) {
        TaskWithProject myNewTaskWithProject = new TaskWithProject();
        myNewTaskWithProject.setTask(task);
        myNewTaskWithProject.setProject(ProjectsList.get((int) task.getProjectId()-1));
        return myNewTaskWithProject;
    }

    /**
     * Create and return a default list of project for testing
     *
     * @return a default list of project
     */
    public List<Project> getDefaultProjectList() {
        List<Project> defaultProjectList = new ArrayList<>();
        defaultProjectList.add(new Project(1, "Projet Tartampion", -1385775));
        defaultProjectList.add(new Project(2, "Projet Lucidia", -4928070));
        defaultProjectList.add(new Project(3, "Projet Circus", -6041902));
        defaultProjectList.add(new Project(4, "Projet Puce Muse", -16733526));
        return defaultProjectList;
    }
}