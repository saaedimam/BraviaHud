package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class NotificationCompat extends android.support.v4.app.NotificationCompat {
    @Deprecated
    public NotificationCompat() {
    }

    @Deprecated
    public static MediaSessionCompat.Token getMediaSession(Notification notification) {
        Bundle extras = getExtras(notification);
        if (extras == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Parcelable parcelable = extras.getParcelable(android.support.v4.app.NotificationCompat.EXTRA_MEDIA_SESSION);
            if (parcelable != null) {
                return MediaSessionCompat.Token.fromToken(parcelable);
            }
            return null;
        }
        IBinder binder = BundleCompat.getBinder(extras, android.support.v4.app.NotificationCompat.EXTRA_MEDIA_SESSION);
        if (binder == null) {
            return null;
        }
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.writeStrongBinder(binder);
        parcelObtain.setDataPosition(0);
        MediaSessionCompat.Token tokenCreateFromParcel = MediaSessionCompat.Token.CREATOR.createFromParcel(parcelObtain);
        parcelObtain.recycle();
        return tokenCreateFromParcel;
    }

    @Deprecated
    public static class Builder extends android.support.v4.app.NotificationCompat.Builder {
        @Deprecated
        public Builder(Context context) {
            super(context);
        }
    }

    @Deprecated
    public static class MediaStyle extends android.support.v4.media.app.NotificationCompat.MediaStyle {
        @Deprecated
        public MediaStyle() {
        }

        @Deprecated
        public MediaStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            super(builder);
        }

        @Override // android.support.v4.media.app.NotificationCompat.MediaStyle
        @Deprecated
        public MediaStyle setShowActionsInCompactView(int... iArr) {
            return (MediaStyle) super.setShowActionsInCompactView(iArr);
        }

        @Override // android.support.v4.media.app.NotificationCompat.MediaStyle
        @Deprecated
        public MediaStyle setMediaSession(MediaSessionCompat.Token token) {
            return (MediaStyle) super.setMediaSession(token);
        }

        @Override // android.support.v4.media.app.NotificationCompat.MediaStyle
        @Deprecated
        public MediaStyle setShowCancelButton(boolean z) {
            return (MediaStyle) super.setShowCancelButton(z);
        }

        @Override // android.support.v4.media.app.NotificationCompat.MediaStyle
        @Deprecated
        public MediaStyle setCancelButtonIntent(PendingIntent pendingIntent) {
            return (MediaStyle) super.setCancelButtonIntent(pendingIntent);
        }
    }

    @Deprecated
    public static class DecoratedCustomViewStyle extends android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle {
        @Deprecated
        public DecoratedCustomViewStyle() {
        }
    }

    @Deprecated
    public static class DecoratedMediaCustomViewStyle extends android.support.v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle {
        @Deprecated
        public DecoratedMediaCustomViewStyle() {
        }
    }
}
