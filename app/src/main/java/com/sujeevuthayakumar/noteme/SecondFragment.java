package com.sujeevuthayakumar.noteme;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.sujeevuthayakumar.noteme.databinding.FragmentSecondBinding;

import java.io.ByteArrayOutputStream;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private NoteModel noteModel;
    private SharedNoteViewModel viewModel;
    private String noteColor;
    private ImageView imageView;
    private String currentPhotoPath;

    private final ActivityResultLauncher<String> galleryPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            selectImageFromGallery();
        }
    });

    private final ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            captureImageFromCamera();
        }
    });

    private final ActivityResultLauncher<Intent> selectImageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri selectedImageUri = result.getData().getData();
            imageView.setImageURI(selectedImageUri);
        }
    });

    private final ActivityResultLauncher<Intent> captureImageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    });

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
                if (data.getImage() != null) {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(data.getImage(), 0, data.getImage().length));
                }
            }
        });

        imageView = binding.image;
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
                        if (imageView.getDrawable() != null){
                            updateNoteModel.setImage(bitmapToByteArray(((BitmapDrawable) imageView.getDrawable()).getBitmap()));
                        }
                        dataBaseHelper.updateOne(updateNoteModel);
                        Toast.makeText(getContext(), "Note Successfully Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        NoteModel noteModel;
                        try {
                            noteModel = new NoteModel(-1, title, subTitle, note, noteColor);
                            if (imageView.getDrawable() != null){
                                noteModel.setImage(bitmapToByteArray(((BitmapDrawable) imageView.getDrawable()).getBitmap()));
                            }
                        } catch (Exception e) {
                            noteModel = new NoteModel(-1, "error", "error", "error", "error");
                        }

                        boolean success = dataBaseHelper.addOne(noteModel);
                        System.out.printf(noteModel.toString());
                        Toast.makeText(getContext(), "Note Successfully Added", Toast.LENGTH_SHORT).show();
                    }

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);

                }
                styleErrorInputs(view);
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                if (noteModel != null) {
                    dataBaseHelper.deleteOne(noteModel.getId());
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                } else {
                    Toast.makeText(getContext(), "Note Cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
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

        binding.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                } else {
                    captureImageFromCamera();
                }
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

    private void selectImageFromGallery() {
        Intent selectImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageActivityResultLauncher.launch(selectImageIntent);
    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageActivityResultLauncher.launch(takePictureIntent);
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

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}