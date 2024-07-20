package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.repository.WorkmateRepository;

import java.util.List;

public class WorkmateViewModel extends ViewModel {

    private final WorkmateRepository workmateRepository;

    public WorkmateViewModel() {
        workmateRepository = WorkmateRepository.getInstance();
    }

    public LiveData<List<Workmate>> getWorkmatesList() {
        return workmateRepository.getWorkmatesList();
    }

}
