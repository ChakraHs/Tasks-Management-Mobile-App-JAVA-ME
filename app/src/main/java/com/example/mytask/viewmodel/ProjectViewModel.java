package com.example.mytask.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytask.models.Project;
import com.example.mytask.repository.ProjectRepository;

import java.util.List;

public class ProjectViewModel extends ViewModel {
    private LiveData<List<Project>> projects;
    private ProjectRepository projectRepository;

    public ProjectViewModel() {
        projectRepository = new ProjectRepository();
        projects = projectRepository.getAllProjects();
    }

    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    // Add other methods to interact with data like updating the task status
    public void updateProjectStatus(String projectId, boolean isDone) {
        projectRepository.updateProjectStatus(projectId, isDone);
    }
}
