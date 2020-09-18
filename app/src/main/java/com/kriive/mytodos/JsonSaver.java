package com.kriive.mytodos;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class JsonSaver implements InterfaceDataPersistance {
    private Context mContext;
    private static final String SAVE_FILE_NAME = "save.json";

    public JsonSaver(final Context ctx) {
        mContext = ctx;
    }

    @Override
    public ArrayList<ToDo> loadToDos() {
        Log.d("TAG", "Loading data from internal storage...");

        Gson gson = new Gson();

        //Create a StringBuffer used to append the chat read from the file
        StringBuffer strContent = new StringBuffer("");

        int ch;

        try {
            //Open the FileInputStream using the Context object
            FileInputStream fis = mContext.openFileInput(SAVE_FILE_NAME);

            //While to read char by char the file (It is not the only way ! You can also read line by line)
            while ((ch = fis.read()) != -1)
                strContent.append((char) ch);

            //Close the input stream
            fis.close();
        }  catch (IOException e) {
            Log.d("IO", e.getMessage());
            return new ArrayList<>();
        }

        Type collectionType = new TypeToken<Collection<ToDo>>(){}.getType();

        //Create a Type object for the deserialization of the ArrayList of LogDescriptor
        Collection<ToDo> saveList = gson.fromJson(strContent.toString(), collectionType);

        //Save the retrieved list
        if(saveList != null)
            return new ArrayList<ToDo>(saveList);

        return new ArrayList<ToDo>();
    }

    @Override
    public Boolean saveToDos(ArrayList<ToDo> todos) {
        Gson gson = new Gson();

        Log.d("TAG", "Saving data to internal storage...");
        //Create a Type object for the serialization of the ArrayList of LogDescriptor
        Type collectionType = new TypeToken<Collection<ToDo>>(){}.getType();

        //Serialize in JSON
        String todoListSerialized = gson.toJson(todos, collectionType);

        //Write the obtained string on file
        try {
            FileOutputStream fos = mContext.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(todoListSerialized.getBytes());
            fos.close();
        } catch (IOException io) {
            Log.e("IO", io.getMessage());
            return false;
        }

        return true;
    }
}
