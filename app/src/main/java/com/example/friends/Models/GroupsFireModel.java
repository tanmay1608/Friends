package com.example.friends.Models;

import java.io.Serializable;

public class GroupsFireModel implements Serializable {

    String groupName;
    String groupImage;


    public GroupsFireModel(String groupName, String groupImage) {
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    public GroupsFireModel() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }
}
