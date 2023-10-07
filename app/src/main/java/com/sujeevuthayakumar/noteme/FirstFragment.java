package com.sujeevuthayakumar.noteme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sujeevuthayakumar.noteme.databinding.FragmentFirstBinding;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        List<NoteModel> noteModelList = dataBaseHelper.getEveryone();

        ArrayAdapter<NoteModel> noteArrayAdapter = new ArrayAdapter<NoteModel>(getContext(), android.R.layout.simple_list_item_1, noteModelList);

        System.out.println(noteModelList);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getContext(), noteModelList);
        binding.listview.setAdapter(customBaseAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customBaseAdapter.getFilter().filter(query);
                System.out.println(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}