package com.rohitneel.photopixelpro.photocollage.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderListContent {
    public static final List<FolderItem> FOLDERS = new ArrayList();
    public static final Map<String, FolderItem> FOLDERS_MAP = new HashMap();
    public static FolderItem selectedFolder;
    public static int selectedFolderIndex;

    public static FolderItem getSelectedFolder() {
        return selectedFolder;
    }

    public static void setSelectedFolder(FolderItem currentFolder, int index) {
        selectedFolder = currentFolder;
        selectedFolderIndex = index;
    }

    public static void clear() {
        FOLDERS.clear();
        FOLDERS_MAP.clear();
    }

    public static void addItem(FolderItem item) {
        FOLDERS.add(item);
        FOLDERS_MAP.put(item.path, item);
    }

    public static FolderItem getItem(String folderPath) {
        Map<String, FolderItem> map = FOLDERS_MAP;
        if (map.containsKey(folderPath)) {
            return map.get(folderPath);
        }
        return null;
    }
}
