package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
@RequiresApi(9)
class AppCompatTextHelper {

    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle = 0;
    final TextView mView;

    static AppCompatTextHelper create(TextView textView) {
        if (Build.VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }

    void loadFromAttributes(AttributeSet attributeSet, int i) {
        boolean z;
        boolean z2;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextHelper, i, 0);
        int resourceId = tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (tintTypedArrayObtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager, tintTypedArrayObtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        tintTypedArrayObtainStyledAttributes.recycle();
        boolean z3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        ColorStateList colorStateList3 = null;
        if (resourceId != -1) {
            TintTypedArray tintTypedArrayObtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, R.styleable.TextAppearance);
            if (z3 || !tintTypedArrayObtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                z = false;
                z2 = false;
            } else {
                z2 = tintTypedArrayObtainStyledAttributes2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                z = true;
            }
            updateTypefaceAndStyle(context, tintTypedArrayObtainStyledAttributes2);
            if (Build.VERSION.SDK_INT < 23) {
                ColorStateList colorStateList4 = tintTypedArrayObtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColor) ? tintTypedArrayObtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColor) : null;
                colorStateList2 = tintTypedArrayObtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorHint) ? tintTypedArrayObtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorHint) : null;
                ColorStateList colorStateList5 = colorStateList4;
                colorStateList = tintTypedArrayObtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorLink) ? tintTypedArrayObtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorLink) : null;
                colorStateList3 = colorStateList5;
            } else {
                colorStateList = null;
                colorStateList2 = null;
            }
            tintTypedArrayObtainStyledAttributes2.recycle();
        } else {
            z = false;
            z2 = false;
            colorStateList = null;
            colorStateList2 = null;
        }
        TintTypedArray tintTypedArrayObtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.TextAppearance, i, 0);
        if (!z3 && tintTypedArrayObtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            z2 = tintTypedArrayObtainStyledAttributes3.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            z = true;
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (tintTypedArrayObtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColor)) {
                colorStateList3 = tintTypedArrayObtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (tintTypedArrayObtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                colorStateList2 = tintTypedArrayObtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            if (tintTypedArrayObtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                colorStateList = tintTypedArrayObtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            }
        }
        updateTypefaceAndStyle(context, tintTypedArrayObtainStyledAttributes3);
        tintTypedArrayObtainStyledAttributes3.recycle();
        if (colorStateList3 != null) {
            this.mView.setTextColor(colorStateList3);
        }
        if (colorStateList2 != null) {
            this.mView.setHintTextColor(colorStateList2);
        }
        if (colorStateList != null) {
            this.mView.setLinkTextColor(colorStateList);
        }
        if (!z3 && z) {
            setAllCaps(z2);
        }
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
        if (Build.VERSION.SDK_INT < 26 || this.mAutoSizeTextHelper.getAutoSizeTextType() == 0) {
            return;
        }
        int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
        if (autoSizeTextAvailableSizes.length > 0) {
            if (this.mView.getAutoSizeStepGranularity() != -1.0f) {
                this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
                this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextAvailableSizes, 0);
            }
        }
    }

    private void updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        this.mStyle = tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) || tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            this.mFontTypeface = null;
            int i = tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) ? R.styleable.TextAppearance_android_fontFamily : R.styleable.TextAppearance_fontFamily;
            if (!context.isRestricted()) {
                try {
                    this.mFontTypeface = tintTypedArray.getFont(i, this.mStyle, this.mView);
                } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.mFontTypeface == null) {
                this.mFontTypeface = Typeface.create(tintTypedArray.getString(i), this.mStyle);
            }
        }
    }

    void onSetTextAppearance(Context context, int i) {
        ColorStateList colorStateList;
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, i, R.styleable.TextAppearance);
        if (tintTypedArrayObtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            setAllCaps(tintTypedArrayObtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23 && tintTypedArrayObtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor) && (colorStateList = tintTypedArrayObtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor)) != null) {
            this.mView.setTextColor(colorStateList);
        }
        updateTypefaceAndStyle(context, tintTypedArrayObtainStyledAttributes);
        tintTypedArrayObtainStyledAttributes.recycle();
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
    }

    void setAllCaps(boolean z) {
        this.mView.setAllCaps(z);
    }

    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint == null && this.mDrawableTopTint == null && this.mDrawableRightTint == null && this.mDrawableBottomTint == null) {
            return;
        }
        Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
        applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
        applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
        applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
        applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
    }

    final void applyCompoundDrawableTint(Drawable drawable, TintInfo tintInfo) {
        if (drawable == null || tintInfo == null) {
            return;
        }
        AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
    }

    protected static TintInfo createTintInfo(Context context, AppCompatDrawableManager appCompatDrawableManager, int i) {
        ColorStateList tintList = appCompatDrawableManager.getTintList(context, i);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT < 26) {
            autoSizeText();
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    void setTextSize(int i, float f) {
        if (Build.VERSION.SDK_INT >= 26 || isAutoSizeEnabled()) {
            return;
        }
        setTextSizeInternal(i, f);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    private void setTextSizeInternal(int i, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(i, f);
    }

    void setAutoSizeTextTypeWithDefaults(int i) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(i);
    }

    void setAutoSizeTextTypeUniformWithConfiguration(int i, int i2, int i3, int i4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
    }

    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] iArr, int i) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
    }

    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }
}
