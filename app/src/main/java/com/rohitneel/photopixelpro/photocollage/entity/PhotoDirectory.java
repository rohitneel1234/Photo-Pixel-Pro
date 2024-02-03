package com.rohitneel.photopixelpro.photocollage.entity;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.rohitneel.photopixelpro.photocollage.picker.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoDirectory implements Comparable<PhotoDirectory> {
    private String coverPath;
    private long dateAdded;

    private String id;
    private String name;
    private List<Photo> photos = new ArrayList();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhotoDirectory)) {
            return false;
        }
        PhotoDirectory photoDirectory = (PhotoDirectory) obj;
        boolean z = !TextUtils.isEmpty(this.id);
        boolean isEmpty = true ^ TextUtils.isEmpty(photoDirectory.id);
        if (!z || !isEmpty || !TextUtils.equals(this.id, photoDirectory.id)) {
            return false;
        }
        return TextUtils.equals(this.name, photoDirectory.name);
    }

    public int hashCode() {
        if (!TextUtils.isEmpty(this.id)) {
            int hashCode = this.id.hashCode();
            if (TextUtils.isEmpty(this.name)) {
                return hashCode;
            }
            return (hashCode * 31) + this.name.hashCode();
        } else if (TextUtils.isEmpty(this.name)) {
            return 0;
        } else {
            return this.name.hashCode();
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public void setCoverPath(String str) {
        this.coverPath = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }


    public void setDateAdded(long j) {
        this.dateAdded = j;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<Photo> list) {
        if (list != null) {
            int size = list.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                Photo photo = list.get(i);
                if (photo == null || !FileUtils.fileIsExists(photo.getPath())) {
                    list.remove(i);
                } else {
                    i++;
                }
            }
            this.photos = list;
        }
    }

    public List<String> getPhotoPaths() {
        ArrayList arrayList = new ArrayList(this.photos.size());
        for (Photo path : this.photos) {
            arrayList.add(path.getPath());
        }
        return arrayList;
    }

    public void addPhoto(int i, String str) {
        if (FileUtils.fileIsExists(str)) {
            this.photos.add(new Photo(i, str));
        }
    }

    public int compareTo(@NonNull PhotoDirectory photoDirectory) {
        try {
            return this.name.compareTo(photoDirectory.getName());
        } catch (Exception e) {
            return 1;
        }
    }
}
