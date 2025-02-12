package com.jaredrummler.android.colorpicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.util.Supplier;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * A Preference to select a color
 */
public class ColorPreferenceCompat extends Preference implements ColorPickerDialogListener {

  private static final int SIZE_NORMAL = 0;
  private static final int SIZE_LARGE = 1;

  private final DialogSupport dialogSupport;

  private OnShowDialogListener onShowDialogListener;
  private int color = Color.BLACK;
//  private boolean showDialog;
//  @ColorPickerDialog.DialogType private int dialogType;
//  private int colorShape;
//  private boolean allowPresets;
//  private boolean allowCustom;
//  private boolean showAlphaSlider;
//  private boolean showColorShades;
//  private int previewSize;
//  private int[] presets;
//  private int dialogTitle;

  private static class DialogSupport {
    private final boolean showDialog;
    @ColorPickerDialog.DialogType private int dialogType;
    private final int colorShape;
    private final boolean allowPresets;
    private final boolean allowCustom;
    private final boolean showAlphaSlider;
    private final boolean showColorShades;
    private final int previewSize;
    private int[] presets;
    private final int dialogTitle;
    private int color = Color.BLACK;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private DialogSupport(TypedArray a, Supplier<Context> contextSupplier, IntConsumer layoutSetter) {
      showDialog = a.getBoolean(R.styleable.ColorPreference_cpv_showDialog, true);
      dialogType = a.getInt(R.styleable.ColorPreference_cpv_dialogType, ColorPickerDialog.TYPE_PRESETS);
      colorShape = a.getInt(R.styleable.ColorPreference_cpv_colorShape, ColorShape.CIRCLE);
      allowPresets = a.getBoolean(R.styleable.ColorPreference_cpv_allowPresets, true);
      allowCustom = a.getBoolean(R.styleable.ColorPreference_cpv_allowCustom, true);
      showAlphaSlider = a.getBoolean(R.styleable.ColorPreference_cpv_showAlphaSlider, false);
      showColorShades = a.getBoolean(R.styleable.ColorPreference_cpv_showColorShades, true);
      previewSize = a.getInt(R.styleable.ColorPreference_cpv_previewSize, SIZE_NORMAL);
      final int presetsResId = a.getResourceId(R.styleable.ColorPreference_cpv_colorPresets, 0);
      dialogTitle = a.getResourceId(R.styleable.ColorPreference_cpv_dialogTitle, R.string.cpv_default_title);
      if (presetsResId != 0) {
        presets = contextSupplier.get().getResources().getIntArray(presetsResId);
      } else {
        presets = ColorPickerDialog.MATERIAL_COLORS;
      }
      final int finalLayoutCons;
      if (colorShape == ColorShape.CIRCLE) {
//        setWidgetLayoutResource(
                finalLayoutCons = previewSize == SIZE_LARGE ? R.layout.cpv_preference_circle_large : R.layout.cpv_preference_circle;
//        );
      } else {
//        setWidgetLayoutResource(
                finalLayoutCons = previewSize == SIZE_LARGE ? R.layout.cpv_preference_square_large : R.layout.cpv_preference_square;
//        );
      }
      layoutSetter.accept(finalLayoutCons);
      a.recycle();

    }

    void onClick(Supplier<FragmentActivity> activitySupplier, String tag) {
      if (showDialog) {
//        ColorPickerDialog.newBuilder(1)
      ColorPickerDialog dialog = ColorPickerDialog.newBuilder(1)
                .setDialogType(dialogType)
                .setDialogTitle(dialogTitle)
                .setColorShape(colorShape)
                .setPresets(presets)
                .setAllowPresets(allowPresets)
                .setAllowCustom(allowCustom)
                .setShowAlphaSlider(showAlphaSlider)
                .setShowColorShades(showColorShades)
                .setColor(color)
              .create();
//                .show(getActivity());

        //      dialog.setColorPickerDialogListener(this);
      activitySupplier.get().getSupportFragmentManager()
          .beginTransaction()
          .add(dialog, tag)
          .commitAllowingStateLoss();

      }
    }

    void onAttached(Supplier<FragmentActivity> activitySupplier, String tag, ColorPickerDialogListener listener) {
      if (showDialog) {
        ColorPickerDialog fragment =
                (ColorPickerDialog) activitySupplier.get().getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
          // re-bind preference to fragment
          fragment.setColorPickerDialogListener(listener);
        }
      }
    }

//    /**
//     * Get the colors that will be shown in the {@link ColorPickerDialog}.
//     *
//     * @return An array of color ints
//     */
//    public int[] getPresets() {
//      return presets;
//    }
//
//    /**
//     * Set the colors shown in the {@link ColorPickerDialog}.
//     *
//     * @param presets An array of color ints
//     */
//    public void setPresets(@NonNull int[] presets) {
//      this.presets = presets;
//    }

  }

  @SuppressLint("NewApi")
  public ColorPreferenceCompat(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray ta = init(attrs);
    dialogSupport = new DialogSupport(ta, this::getContext, this::setWidgetLayoutResource);
    ta.recycle();
  }

  @SuppressLint("NewApi")
  public ColorPreferenceCompat(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    TypedArray ta = init(attrs);
    dialogSupport = new DialogSupport(ta, this::getContext, this::setWidgetLayoutResource);
    ta.recycle();
  }

  private TypedArray init(AttributeSet attrs) {
    setPersistent(true);
    return getContext().obtainStyledAttributes(attrs, R.styleable.ColorPreference);
//    showDialog = a.getBoolean(R.styleable.ColorPreference_cpv_showDialog, true);
//    //noinspection WrongConstant
//    dialogType = a.getInt(R.styleable.ColorPreference_cpv_dialogType, ColorPickerDialog.TYPE_PRESETS);
//    colorShape = a.getInt(R.styleable.ColorPreference_cpv_colorShape, ColorShape.CIRCLE);
//    allowPresets = a.getBoolean(R.styleable.ColorPreference_cpv_allowPresets, true);
//    allowCustom = a.getBoolean(R.styleable.ColorPreference_cpv_allowCustom, true);
//    showAlphaSlider = a.getBoolean(R.styleable.ColorPreference_cpv_showAlphaSlider, false);
//    showColorShades = a.getBoolean(R.styleable.ColorPreference_cpv_showColorShades, true);
//    previewSize = a.getInt(R.styleable.ColorPreference_cpv_previewSize, SIZE_NORMAL);
//    final int presetsResId = a.getResourceId(R.styleable.ColorPreference_cpv_colorPresets, 0);
//    dialogTitle = a.getResourceId(R.styleable.ColorPreference_cpv_dialogTitle, R.string.cpv_default_title);
//    if (presetsResId != 0) {
//      presets = getContext().getResources().getIntArray(presetsResId);
//    } else {
//      presets = ColorPickerDialog.MATERIAL_COLORS;
//    }
//    if (colorShape == ColorShape.CIRCLE) {
//      setWidgetLayoutResource(
//          previewSize == SIZE_LARGE ? R.layout.cpv_preference_circle_large : R.layout.cpv_preference_circle);
//    } else {
//      setWidgetLayoutResource(
//          previewSize == SIZE_LARGE ? R.layout.cpv_preference_square_large : R.layout.cpv_preference_square);
//    }
//    a.recycle();
  }

  @Override protected void onClick() {
    super.onClick();
    if (onShowDialogListener != null) {
      onShowDialogListener.onShowColorPickerDialog((String) getTitle(), color);
    } else /*if (showDialog)*/ {
      dialogSupport.onClick(this::getActivity, getFragmentTag());
//    } else if (showDialog) {
//      ColorPickerDialog.newBuilder(1)
////      ColorPickerDialog dialog = ColorPickerDialog.newBuilder(1)
//          .setDialogType(dialogType)
//          .setDialogTitle(dialogTitle)
//          .setColorShape(colorShape)
//          .setPresets(presets)
//          .setAllowPresets(allowPresets)
//          .setAllowCustom(allowCustom)
//          .setShowAlphaSlider(showAlphaSlider)
//          .setShowColorShades(showColorShades)
//          .setColor(color)
//              .show(getActivity());
//          .create();
//      dialog.setColorPickerDialogListener(this);
//      getActivity().getSupportFragmentManager()
//          .beginTransaction()
//          .add(dialog, getFragmentTag())
//          .commitAllowingStateLoss();
    }
  }

  public FragmentActivity getActivity() {
    Context context = getContext();
    if (context instanceof FragmentActivity) {
      return (FragmentActivity) context;
    } else if (context instanceof ContextWrapper) {
      Context baseContext = ((ContextWrapper) context).getBaseContext();
      if (baseContext instanceof FragmentActivity) {
        return (FragmentActivity) baseContext;
      }
    }
    throw new IllegalStateException("Error getting activity from context");
  }

  @Override public void onAttached() {
    super.onAttached();
    dialogSupport.onAttached(this::getActivity, getFragmentTag(), this);
//    if (showDialog) {
//      ColorPickerDialog fragment =
//          (ColorPickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(getFragmentTag());
//      if (fragment != null) {
//        // re-bind preference to fragment
//        fragment.setColorPickerDialogListener(this);
//      }
//    }
  }

  @Override public void onBindViewHolder(PreferenceViewHolder holder) {
    super.onBindViewHolder(holder);
    ColorPanelView preview = (ColorPanelView) holder.itemView.findViewById(R.id.cpv_preference_preview_color_panel);
    if (preview != null) {
      preview.setColor(color);
    }
  }

  @Override protected void onSetInitialValue(Object defaultValue) {
    super.onSetInitialValue(defaultValue);
    if (defaultValue instanceof Integer) {
      color = (Integer) defaultValue;
      persistInt(color);
    } else {
      color = getPersistedInt(0xFF000000);
    }
  }

  @Override protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getInteger(index, Color.BLACK);
  }

  @Override public void onColorSelected(int dialogId, @ColorInt int color) {
    saveValue(color);
  }

  @Override public void onDialogDismissed(int dialogId) {
    // no-op
  }

  /**
   * Set the new color
   *
   * @param color The newly selected color
   */
  public void saveValue(@ColorInt int color) {
    this.color = color;
    persistInt(this.color);
    notifyChanged();
    callChangeListener(color);
  }

  /**
   * Get the colors that will be shown in the {@link ColorPickerDialog}.
   *
   * @return An array of color ints
   */
  public int[] getPresets() {
    return dialogSupport.presets;
  }

  /**
   * Set the colors shown in the {@link ColorPickerDialog}.
   *
   * @param presets An array of color ints
   */
  public void setPresets(@NonNull int[] presets) {
    dialogSupport.presets = presets;
  }

  /**
   * The listener used for showing the {@link ColorPickerDialog}.
   * Call {@link #saveValue(int)} after the user chooses a color.
   * If this is set then it is up to you to show the dialog.
   *
   * @param listener The listener to show the dialog
   */
  public void setOnShowDialogListener(OnShowDialogListener listener) {
    onShowDialogListener = listener;
  }

  /**
   * The tag used for the {@link ColorPickerDialog}.
   *
   * @return The tag
   */
  public String getFragmentTag() {
    return "color_" + getKey();
  }

  public interface OnShowDialogListener {

    void onShowColorPickerDialog(String title, int currentColor);
  }
}
