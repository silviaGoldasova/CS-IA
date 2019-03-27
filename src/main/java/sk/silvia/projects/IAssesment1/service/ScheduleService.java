package sk.silvia.projects.IAssesment1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.silvia.projects.IAssesment1.Dao.TaskRepository;
import sk.silvia.projects.IAssesment1.Dao.UploadedDataRepository;
import sk.silvia.projects.IAssesment1.dto.UploadedDTO;
import sk.silvia.projects.IAssesment1.model.entity.CompletedTask;
import sk.silvia.projects.IAssesment1.model.entity.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UploadedDataRepository uploadedDataRepository;

    @Autowired
    private TaskService taskService;

    public List<Task> generateSchedule(int sessionLength, int breakLength, int breakFrequency, String categorySelected) {
        List<Task> selected = taskService.getSelectedTaskList();
        List<Task> list = getCategoryTasks(categorySelected, selected);
        List<Task> display = new LinkedList<>();
        List<Integer> durations = new LinkedList<>();

        int durationsTotal = 0;
        int duration = 0;
        Task task;

        // get info about all tasks in the category
        for(int i = 0; i < list.size(); i++) {
            task = list.get(i);
            durations.add(task.getDuration());
            durationsTotal += task.getDuration();
        }

        if(durationsTotal < sessionLength ){       // if(durationsTotal < schedule.getSessionLength() + (schedule.getSessionLength()/schedule.getBreakFrequency())*schedule.getBreakLength() ){
            display = list;
        }
        else {
            Task[] sorted = sortTasks(getCategoryTasks(categorySelected, taskRepository.findAll()));

            //fill the display list with shortest and the longest tasks available in the desired category while the total durations of the tasks are <= to desired session length
            int i = 0, back;
            while(duration + sorted[i].getDuration() <= sessionLength && ( (i < (sorted.length/2)) || ( (sorted.length%2 == 1) && (i <= (sorted.length/2)))  )   ){
                display.add(sorted[i]);
                duration = duration + sorted[i].getDuration();

                back = sorted.length-i-1;
                if (duration + sorted[back].getDuration() <= sessionLength &&  i < (sorted.length/2)  ) {
                    display.add(sorted[back]);
                    duration += sorted[back].getDuration();
                }
                i++;
            }
        }
        return display;
    }

    public List<UploadedDTO> getDataForUpload(List<Task> display, int breakFrequency, int breakLength) {
        List<UploadedDTO> list = new ArrayList<>();

        int remainder = 0;
        int dur = 0;
        int i = 0;
        int previous_remainder = 0;
        int switchTime = 5;

        while (i < display.size()) {
            dur = display.get(i).getDuration();

            while (dur >= breakFrequency-previous_remainder) {
                list.add(new UploadedDTO(display.get(i).getName(), breakFrequency-previous_remainder));
                list.add(new UploadedDTO("Break", breakLength));

                dur = dur - (breakFrequency - previous_remainder);
                remainder = dur;
                previous_remainder = 0;
            }
            if (dur < breakFrequency-previous_remainder) {
                list.add(new UploadedDTO(display.get(i).getName(), dur));
                remainder = 0;
                previous_remainder = dur;
            }
            if(remainder == 0 && i != display.size()-1) {
                list.add(new UploadedDTO("next:" + display.get(i+1).getName(), switchTime));
            }
            i++;
        }

        for(int j = 0; j < list.size(); j++) {
            System.out.println(j + ". " + list.get(j).getName() + "   " + list.get(j).getDuration());
        }

        saveUploadDataToRepository(list);

        taskService.setTasksForUpload(display);
        return list;
    }

    public Integer getTotalDuration(List<UploadedDTO> list) {
        int totalDuration = 0;
        for (int i = 0; i < list.size(); i++) {
            totalDuration += list.get(i).getDuration();
            System.out.println(i + ". " + list.get(i).getDuration());
        }
        return totalDuration;
    }

    public void buttonsGenerateSchedule(String functionToPerform) {
        switch(functionToPerform) {
            case "decline":
                taskService.setTasksForUploadFalse();
                break;
            default:
                break;
        }
    }

    private void saveUploadDataToRepository(List<UploadedDTO> list){
        clearUploadDataRepository();

        UploadedDTO uploadedDTO;
        for (int i = 0; i < list.size(); i++) {
            uploadedDTO = list.get(i);
            uploadedDataRepository.save(uploadedDTO);
        }
    }

    public List<UploadedDTO> getUploadDataFromRepository(){
        List<UploadedDTO> allList = uploadedDataRepository.findAll();
        return allList;
    }

    private void clearUploadDataRepository(){
        uploadedDataRepository.deleteAll();
    }

    public static List<Task> getCategoryTasks(String category, List<Task> tasks){
        List<Task> list = new LinkedList<Task>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskCategory().equals(category)) {
                list.add(tasks.get(i));
            }
        }
        return list;
    }

    public static Task[] sortTasks(List<Task> tasks){
        Task[] sorted = new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            sorted[i] = tasks.get(i);
        }
        sort(sorted, 0, sorted.length-1);
        return sorted;
    }

    public static int partition(Task arr[], int low, int high) {
        int pivot = arr[high].getDuration();
        int i = (low-1);
        for (int j=low; j<high; j++) {
            if (arr[j].getDuration() <= pivot)  {
                i++;
                Task temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Task temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;
        return i+1;
    }

    public static void sort(Task arr[], int low, int high)  {
        if (low < high) {
            int pi = partition(arr, low, high);
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }


}
