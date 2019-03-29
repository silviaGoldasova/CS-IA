package sk.silvia.projects.iassesment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.silvia.projects.iassesment.dao.CompletedTasksRepository;
import sk.silvia.projects.iassesment.dao.TaskRepository;
import sk.silvia.projects.iassesment.dao.UserRepository;
import sk.silvia.projects.iassesment.entity.CompletedTask;
import sk.silvia.projects.iassesment.entity.MyUser;
import sk.silvia.projects.iassesment.entity.Task;

import java.util.LinkedList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    CompletedTasksRepository completedTasksRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public List<Task> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
        return taskList;
    }

    public void createTask(String name, int duration, String taskCategory ) {
        Task task = new Task();
        task.setDuration(duration);
        if (taskCategory == null) {
            task.setTaskCategory("Other");
        } else {
            task.setTaskCategory(taskCategory);
        }
        task.setName(name);
        taskRepository.save(task);

        userService.registerUser("user", "password");

    }

    public Task viewEditTask(Long id) {
        // load task from database - find task by id
        Task task = taskRepository.getOne(id);
        return task;
    }

    public void saveEditTask(Task task) {
        // fill form with task data
        taskRepository.save(task);
    }

    // List<Task> filteredTasks = allTasks.stream().filter(task -> task.isSelected()).collect(Collectors.toList());
    public List<Task> getSelectedTaskList() {
        List<Task> selectedTasksList = new LinkedList<Task>();
        List<Task> allTasks = taskRepository.findAll();
        for(int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).isSelected() == true)
                selectedTasksList.add(allTasks.get(i));
        }
        return selectedTasksList;
    }

    public void edittingTask(Long id, String function) {
        switch(function) {
            case "delete":
                Task task = taskRepository.getOne(id);
                taskRepository.delete(task);
                break;
            case "completed":
                Task task1 = taskRepository.getOne(id);
                CompletedTask completedTask = new CompletedTask(task1);
                completedTask.setTodayAsDate();
                completedTasksRepository.save(completedTask);
                taskRepository.delete(task1);
                break;
            case "selected":
                Task task2 = taskRepository.getOne(id);
                if (task2.isSelected())
                    task2.setSelected(false);
                else
                    task2.setSelected(true);
                taskRepository.save(task2);
                break;
            default:
                break;
        }
    }

    public void changeSelected(Long id, String function){
        switch(function) {
            case "deselect":
                Task task2 = taskRepository.getOne(id);
                task2.setSelected(false);
                taskRepository.save(task2);
                break;
            case "completed":
                Task task1 = taskRepository.getOne(id);
                CompletedTask completedTask = new CompletedTask(task1);
                completedTask.setTodayAsDate();
                completedTasksRepository.save(completedTask);
                taskRepository.delete(task1);
            default:
                break;
        }
    }

    public List<Task> loadSelected() {
        List<Task> selectedTasks = getSelectedTaskList();
        return selectedTasks;
    }

    public List<CompletedTask> listCompleted() {
        List<CompletedTask> completedTaskList = completedTasksRepository.findAll();
        return completedTaskList;
    }

    public  void setTasksForUploadFalse() {
        int i;
        Task task;
        List<Task> allTasks = taskRepository.findAll();
        for(i = 0; i < allTasks.size();i++) {
            task = allTasks.get(i);
            task.setForUpload(false);
            taskRepository.save(task);
        }
    }

    public  void setTasksForUpload(List<Task> tasksForUpload) {
        int i;
        Task task;
        setTasksForUploadFalse();
        for(i = 0; i < tasksForUpload.size();i++) {
            task = tasksForUpload.get(i);
            task.setForUpload(true);
            taskRepository.save(task);
        }
    }

    public static List<Task> getTasksForUpload(List<Task> tasks) {
        List<Task> forUpload = new LinkedList<Task>();
        for( int i = 0; i<tasks.size();i++) {
            if (tasks.get(i).isForUpload() == true) {
                forUpload.add(tasks.get(i));
            }
        }
        return forUpload;
    }


}
