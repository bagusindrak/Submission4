package com.thinking.submission4.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

   private String id;
   private String name;
   private String description;
   private String photo;


   public Movie(Parcel in) {
      id = in.readString();
      name = in.readString();
      description = in.readString();
      photo = in.readString();
   }

   public Movie() {

   }

   public Movie(String id, String name, String description, String photo) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.photo = photo;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(id);
      dest.writeString(name);
      dest.writeString(description);
      dest.writeString(photo);
   }

   @Override
   public int describeContents() {
      return 0;
   }

   public static final Creator<Movie> CREATOR = new Creator<Movie>() {
      @Override
      public Movie createFromParcel(Parcel in) {
         return new Movie(in);
      }

      @Override
      public Movie[] newArray(int size) {
         return new Movie[size];
      }
   };

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getPhoto() {
      return photo;
   }

   public void setPhoto(String Photo) {
      this.photo = Photo;
   }
}
