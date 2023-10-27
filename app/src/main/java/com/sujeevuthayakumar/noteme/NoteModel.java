package com.sujeevuthayakumar.noteme;

import java.util.Arrays;

public class NoteModel {

    private int id;
    private String title;
    private String subTitle;
    private String note;
    private String noteColor;
    private byte[] image;

    public NoteModel(int id, String title, String subTitle, String note, String noteColor) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.note = note;
        this.noteColor = noteColor;
    }

    public NoteModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", note='" + note + '\'' +
                ", noteColor='" + noteColor + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
