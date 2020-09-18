package com.kriive.mytodos;

import java.util.ArrayList;

public interface InterfaceDataPersistance {
    public ArrayList<ToDo> loadToDos();

    public Boolean saveToDos(final ArrayList<ToDo> todos);
}
