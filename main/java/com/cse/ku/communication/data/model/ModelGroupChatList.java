package com.cse.ku.communication.data.model;

public class ModelGroupChatList {

    String GroupId, groupTitle, GroupInfo, GroupIcon, timesTemp, CreateTedBy;

    public ModelGroupChatList(){

    }

    public ModelGroupChatList(String groupId, String groupTitle, String groupInfo, String groupIcon, String timesTemp, String createTedBy) {
        GroupId = groupId;
        this.groupTitle = groupTitle;
        GroupInfo = groupInfo;
        GroupIcon = groupIcon;
        this.timesTemp = timesTemp;
        CreateTedBy = createTedBy;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupInfo() {
        return GroupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        GroupInfo = groupInfo;
    }

    public String getGroupIcon() {
        return GroupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        GroupIcon = groupIcon;
    }

    public String getTimesTemp() {
        return timesTemp;
    }

    public void setTimesTemp(String timesTemp) {
        this.timesTemp = timesTemp;
    }

    public String getCreateTedBy() {
        return CreateTedBy;
    }

    public void setCreateTedBy(String createTedBy) {
        CreateTedBy = createTedBy;
    }
}
