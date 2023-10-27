package com.sujeevuthayakumar.noteme;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.sujeevuthayakumar.noteme.databinding.FragmentSecondBinding;

import org.w3c.dom.Node;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private NoteModel noteModel;
    private SharedNoteViewModel viewModel;
    private String noteColor;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        binding.editNote.setBackgroundColor(getResources().getColor(R.color.lightblue));
        binding.blue.setBackgroundColor(getResources().getColor(R.color.purple));
        binding.blue.setTextColor(Color.WHITE);
        this.noteColor = "blue";

        viewModel = new ViewModelProvider(getActivity()).get(SharedNoteViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                this.noteModel = data;
                binding.editTitle.setText(data.getTitle());
                binding.editSubtitle.setText(data.getSubTitle());
                binding.editNote.setText(data.getNote());
                this.noteColor = data.getNoteColor();
                binding.blue.setBackgroundColor(getResources().getColor(R.color.lightblue));
                if (data.getNoteColor().equals("blue")) {
                    binding.editNote.setBackgroundColor(getResources().getColor(R.color.lightblue));
                    binding.blue.setBackgroundColor(getResources().getColor(R.color.purple));
                } else if (data.getNoteColor().equals("red")) {
                    binding.editNote.setBackgroundColor(getResources().getColor(R.color.lightred));
                    binding.red.setBackgroundColor(getResources().getColor(R.color.purple));
                } else {
                    binding.editNote.setBackgroundColor(getResources().getColor(R.color.lightyellow));
                    binding.yellow.setBackgroundColor(getResources().getColor(R.color.purple));
                }
            }
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!areInputsEmpty(view)) {
                    String title = binding.editTitle.getText().toString();
                    String subTitle = binding.editSubtitle.getText().toString();
                    String note = binding.editNote.getText().toString();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                    if (noteModel != null) {
                        NoteModel updateNoteModel;
                        updateNoteModel = new NoteModel(noteModel.getId(), title, subTitle, note, noteColor);
                        dataBaseHelper.updateOne(updateNoteModel);
                        Toast.makeText(getContext(), "Note Successfully Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        NoteModel noteModel;
                        try {
                            noteModel = new NoteModel(-1, title, subTitle, note, noteColor);
                        } catch (Exception e) {
                            System.out.println("L");
                            noteModel = new NoteModel(-1, "error", "error", "error", "error");
                        }

                        boolean success = dataBaseHelper.addOne(noteModel);
                        Toast.makeText(getContext(), "Note Successfully Added", Toast.LENGTH_SHORT).show();
                    }

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);

                }
                styleErrorInputs(view);
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("hi " + checkedId);
                resetButtons(view);
                RadioButton checkedButton = view.findViewById(checkedId);
                EditText editText = view.findViewById(R.id.edit_note);
                if (checkedId == R.id.blue) {
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.purple));
                    checkedButton.setTextColor(Color.WHITE);
                    noteColor = "blue";
                    editText.setBackgroundColor(getResources().getColor(R.color.lightblue));
                } else if (checkedId == R.id.red) {
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.purple));
                    checkedButton.setTextColor(Color.WHITE);
                    editText.setBackgroundColor(getResources().getColor(R.color.lightred));
                    noteColor = "red";
                } else if (checkedId == R.id.yellow) {
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.purple));
                    checkedButton.setTextColor(Color.WHITE);
                    editText.setBackgroundColor(getResources().getColor(R.color.lightyellow));
                    noteColor = "yellow";
                }
                System.out.println(noteColor);
            }
        });
    }

    private void resetButtons(View view) {
        RadioButton blue = view.findViewById(R.id.blue);
        RadioButton red = view.findViewById(R.id.red);
        RadioButton yellow = view.findViewById(R.id.yellow);

        blue.setTextColor(Color.BLACK);
        red.setTextColor(Color.BLACK);
        yellow.setTextColor(Color.BLACK);

        blue.setBackgroundColor(getResources().getColor(R.color.lightblue));
        red.setBackgroundColor(getResources().getColor(R.color.lightred));
        yellow.setBackgroundColor(getResources().getColor(R.color.lightyellow));
    }

    private boolean areInputsEmpty(View view) {
        String titleText = binding.editTitle.getText().toString();
        return titleText.isEmpty();
    }

    // Function to style inputs based on whether they are required
    private void styleErrorInputs(View view) {
        // Get the values of inputs
        String titleText = binding.editTitle.getText().toString();

        // Color for input error
        int color = Color.rgb(255,0,0);
        int color2 = Color.parseColor("#FF11AA");

        // The states for an input when clicked, and hovered
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_focused},
                new int[] { android.R.attr.state_hovered},
                new int[] { android.R.attr.state_enabled},
                new int[] { }
        };

        // Set all possible colors
        int[] colors = new int[] {
                color,
                color,
                color,
                color2
        };

        // The list of colors used for the state
        ColorStateList myColorList = new ColorStateList(states, colors);

        // Check for principal amount input
        if (titleText.isEmpty()) {
            binding.editTitleLayout.setError("Required");
        } else {
            binding.editTitleLayout.setError(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}