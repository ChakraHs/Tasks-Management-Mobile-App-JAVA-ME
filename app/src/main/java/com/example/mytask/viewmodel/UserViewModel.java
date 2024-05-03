package com.example.mytask.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytask.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<String> getUsername() {
        return userRepository.getUsername();
    }
}
