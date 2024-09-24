package com.hub.contextawaretaskmanagement.Rules.Conditions;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Condition implements Parcelable, Serializable {

    private static int lastJobId = 0;
    private int job_id, batteryLevel;
    private String id, time, logical_operator, phone_id, location;
    private boolean waitToCall, waitToText;


    public Condition() {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.job_id = generateUniqueJobId();
        this.time = null;
        this.logical_operator = null;
        this.phone_id = null;
        this.location = null;
        this.waitToCall = false;
        this.waitToText = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Condition(Parcel in) {
        id = in.readString();
        time = in.readString();
        batteryLevel = in.readInt();
        phone_id = in.readString();
        job_id = generateUniqueJobId();
        waitToCall = in.readBoolean();
        waitToText = in.readBoolean();
        logical_operator = in.readString();
        location = in.readString();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeInt(batteryLevel);
        dest.writeString(phone_id);
        dest.writeInt(job_id);
        dest.writeBoolean(waitToCall);
        dest.writeBoolean(waitToText);
        dest.writeString(logical_operator);
        dest.writeString(location);

    }


    public static final Parcelable.Creator<Condition> CREATOR = new Parcelable.Creator<Condition>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Condition createFromParcel(Parcel in) {
            return new Condition(in);
        }

        @Override
        public Condition[] newArray(int size) {
            return new Condition[size];
        }
    };

    private static synchronized int generateUniqueJobId() {
        return ++lastJobId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getPhoneId() {
        return phone_id;
    }


    public boolean getWaitToCall() {
        return waitToCall;
    }

    public boolean getWaitToText() {
        return waitToText;
    }

    public void setPhoneId(String phone_id) {
        this.phone_id = phone_id;
    }

    public String getId() {
        return this.id;
    }

    public String getLogical_operator() {
        return logical_operator;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Condition {");

        if (this.job_id != 0) result.append("job_id=").append(job_id).append(", ");
        if (this.batteryLevel != 0)
            result.append("batteryLevel=").append(batteryLevel).append(", ");
        if (this.id != null) result.append("id='").append(id).append('\'').append(", ");
        if (this.time != null) result.append("time='").append(time).append('\'').append(", ");
        if (this.logical_operator != null)
            result.append("logical_operator='").append(logical_operator).append('\'').append(", ");
        if (this.phone_id != null)
            result.append("phone_id='").append(phone_id).append('\'').append(", ");
        if (this.location != null)
            result.append("location='").append(location).append('\'').append(", ");
        if (this.waitToCall)
            result.append("Wait_To_Call='").append(waitToCall).append('\'').append(", ");
        if (this.waitToText)
            result.append("Wait_To_Text='").append(waitToText).append('\'').append(", ");
        if (result.charAt(result.length() - 2) == ',') {
            result.setLength(result.length() - 2);
        }

        result.append('}');
        return result.toString();
    }


    public void setLogical_operator(String logical_operator) {
        this.logical_operator = logical_operator;
    }

    public void setWaitToCall(boolean waitToCall) {
        this.waitToCall = waitToCall;
    }

    public void setWaitToText(boolean waitToText) { this.waitToText = waitToText;
    }


    public boolean equalsAND(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Condition)) return false;
        Condition thatObject = (Condition) obj;
        return (this.batteryLevel == thatObject.batteryLevel &&
                Objects.equals(this.time, thatObject.time) &&
                Objects.equals(this.phone_id, thatObject.phone_id) &&
                Objects.equals(this.location, thatObject.location) &&
                Objects.equals(this.waitToCall, thatObject.waitToCall) &&
                Objects.equals(this.waitToText,thatObject.waitToText)
        );
    }

    public boolean equalsOR(Object obj) {
        if (!(obj instanceof Condition)) return false;
        Condition thatObject = (Condition) obj;
        return ((batteryLevel != 0 && batteryLevel == thatObject.batteryLevel) || (time != null && time.equals(thatObject.time)) || (phone_id != null && phone_id.equals(thatObject.phone_id)) || (location != null && location.equals(thatObject.location)) ||
                (waitToCall = thatObject.waitToCall) || (waitToText = thatObject.waitToText));
    }

    public boolean equalsNOT(Object obj) {
        return !equals(obj);
    }

    public boolean equalsXOR(Object obj) {
        if (!(obj instanceof Condition)) return false;
        Condition thatObject = (Condition) obj;

        boolean batteryLevelXor = batteryLevel != 0 ^ thatObject.batteryLevel != 0;
        boolean timeXor = (time != null) ^ (thatObject.time != null);
        boolean phoneIdXor = (phone_id != null) ^ (thatObject.phone_id != null);
        boolean locationXor = (location != null) ^ (thatObject.location != null);
        boolean waitToCallXor = (waitToCall) ^ (thatObject.waitToCall);
        boolean waitToTextXor = (waitToText) ^ (thatObject.waitToText);


        return batteryLevelXor || timeXor || phoneIdXor || locationXor || waitToCallXor || waitToTextXor;
    }


    @Override
    public int hashCode() {
        return Objects.hash(time, batteryLevel, phone_id, location, waitToCall,waitToText);
    }


}
