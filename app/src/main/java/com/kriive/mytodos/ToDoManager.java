package com.kriive.mytodos;

import android.util.Log;

import java.util.ArrayList;

public class ToDoManager {
    private static ToDoManager single_instance = null;
    private ArrayList<ToDo> toDos;
    private OnAddListener onadd;
    private OnDeleteListener ondel;
    private OnEditListener onedit;

    private ToDoManager() {
        toDos = new ArrayList<>();
    }

    public void addToDo(final ToDo newTodo) {
        toDos.add(0, newTodo);
        if (onadd != null) {
            onadd.onAdd(newTodo);
        }
    }

    public void removeToDo(final ToDo toDoDelete) {
        if (ondel != null) {
            ondel.onDelete(toDoDelete);
        }
        toDos.remove(toDoDelete);
    }

    public interface OnAddListener {
        void onAdd(final ToDo todo);
    }

    public interface OnDeleteListener {
        void onDelete(final ToDo todo);
    }

    public interface OnEditListener {
        void onEdit(final ToDo todo);
    }

    public void markToDo(final ToDo todo, final Boolean completed) {
        if (!toDos.contains(todo)) {
            Log.d("TAG", todo.getName() + "isn't contained.");
            return;
        }

        toDos.get(toDos.indexOf(todo)).setCompleted(completed);
        Log.d("TAG", todo.getName() + " " + completed + toDos.get(toDos.indexOf(todo)).getCompleted());
        if (onedit != null) {
            onedit.onEdit(todo);
        }
    }

    public void setOnAddListener(OnAddListener listener) {
        onadd = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        ondel = listener;
    }

    public void setOnEditListener(OnEditListener listener) {
        onedit = listener;
    }

    public static ToDoManager getInstance() {
        if (single_instance == null)
            single_instance = new ToDoManager();

        return single_instance;
    }

    public Boolean isEmpty() {
        return toDos.isEmpty();
    }

    public ArrayList<ToDo> getList() {
        return toDos;
    }

    public Boolean save(final InterfaceDataPersistance saver) {
        return saver.saveToDos(getList());
    }

    public void load(final InterfaceDataPersistance loader) {
        for (final ToDo todo : loader.loadToDos()) {
            if (!toDos.contains(todo)) {
                addToDo(todo);
            }
        }
    }
}