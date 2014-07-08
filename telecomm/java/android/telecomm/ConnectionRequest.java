/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.telecomm;

import android.os.Bundle;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Simple data container encapsulating a request to some entity to
 * create a new {@link Connection}.
 */
public final class ConnectionRequest implements Parcelable {

    // TODO: Token to limit recursive invocations
    // TODO: Consider upgrading "mHandle" to ordered list of handles, indicating a set of phone
    //         numbers that would satisfy the client's needs, in order of preference
    private final String mCallId;
    private final Uri mHandle;
    private final Bundle mExtras;
    private final PhoneAccount mAccount;
    private final int mVideoState;

    public ConnectionRequest(String callId, Uri handle, Bundle extras, int videoState) {
        this(null, callId, handle, extras, videoState);
    }

    public ConnectionRequest(PhoneAccount account, String callId, Uri handle, Bundle extras,
            int videoState) {
        mCallId = callId;
        mHandle = handle;
        mExtras = extras;
        mAccount = account;
        mVideoState = videoState;
    }

    /**
     * The account which should be used to place the call.
     */
    public PhoneAccount getAccount() { return mAccount; }

    /**
     * An identifier for this call.
     */
    public String getCallId() { return mCallId; }

    /**
     * The handle (e.g., phone number) to which the {@link Connection} is to connect.
     */
    public Uri getHandle() { return mHandle; }

    /**
     * Application-specific extra data. Used for passing back information from an incoming
     * call {@code Intent}, and for any proprietary extensions arranged between a client
     * and servant {@code ConnectionService} which agree on a vocabulary for such data.
     */
    public Bundle getExtras() { return mExtras; }

    /**
     * Determines the video state for the connection.
     * Valid values: {@link VideoCallProfile#VIDEO_STATE_AUDIO_ONLY},
     * {@link VideoCallProfile#VIDEO_STATE_BIDIRECTIONAL},
     * {@link VideoCallProfile#VIDEO_STATE_TX_ENABLED},
     * {@link VideoCallProfile#VIDEO_STATE_RX_ENABLED}.
     *
     * @return The video state for the connection.
     */
    public int getVideoState() {
        return mVideoState;
    }

    public String toString() {
        return String.format("PhoneConnectionRequest %s %s",
                mHandle == null
                        ? Uri.EMPTY
                        : ConnectionService.toLogSafePhoneNumber(mHandle.toString()),
                mExtras == null ? "" : mExtras);
    }

    public static final Parcelable.Creator<ConnectionRequest> CREATOR =
            new Parcelable.Creator<ConnectionRequest> () {
                @Override
                public ConnectionRequest createFromParcel(Parcel source) {
                    String callId = source.readString();
                    Uri handle = (Uri) source.readParcelable(getClass().getClassLoader());
                    Bundle extras = (Bundle) source.readParcelable(getClass().getClassLoader());
                    int videoState = source.readInt();
                    return new ConnectionRequest(callId, handle, extras, videoState);
                }

                @Override
                public ConnectionRequest[] newArray(int size) {
                    return new ConnectionRequest[size];
                }
            };

    /**
     * {@inheritDoc}
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeString(mCallId);
        destination.writeParcelable(mHandle, 0);
        destination.writeParcelable(mExtras, 0);
        destination.writeInt(mVideoState);
    }
}
