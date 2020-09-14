package com.kriive.mytodos;

import java.util.ArrayList;

public class ToDoManager {
    private static ToDoManager single_instance = null;
    private ArrayList<ToDo> toDos = new ArrayList<>();
    private OnAddListener onadd;
    private OnDeleteListener ondel;

    private ToDoManager() {
    }

    public void addToDo(final ToDo newTodo) {
        toDos.add(0, newTodo);
        if (onadd != null) {
            onadd.onAdd(0);
        }
    }

    public void removeToDo(final Integer toDoDelete) {
        toDos.remove(toDoDelete);

        if (ondel != null) {
            ondel.onDelete(toDoDelete);
        }
    }

    public interface OnAddListener {
        void onAdd(final Integer pos);
    }

    public interface OnDeleteListener {
        void onDelete(final Integer pos);
    }

    public void setOnAddListener(OnAddListener listener) {
        onadd = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        ondel = listener;
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
}