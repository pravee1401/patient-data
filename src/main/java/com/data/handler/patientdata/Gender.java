package com.data.handler.patientdata;

public enum Gender {

  MALE("MALE"), FEMALE("FEMALE"), OTHER("OTHER");

  private String gender;

  Gender(String gender){
    this.gender = gender;
  }

  public String getGender(){
    return this.gender;
  }
}
