package epeyk.mobile.baseutil.views.simplecropview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import epeyk.mobile.baseutil.R;
import epeyk.mobile.baseutil.views.simplecropview.callback.CropCallback;
import epeyk.mobile.baseutil.views.simplecropview.callback.LoadCallback;
import epeyk.mobile.baseutil.views.simplecropview.callback.SaveCallback;


public class ActivityCropImagePicker extends Activity {

    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_TAKE_IMAGE = 10012;
    private CropImageView mCropView;
    private Dialog progressDialog;
    private Uri cameraFileUri;
    private boolean isSelectedImage = false;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_crop_image_picker);
        bindViews();

        mCropView.setEnabled(false);
        mCropView.setOutputMaxSize(600,600);
        showDialogSelectImage();
    }

    public void showProgress() {
        try {
            if (progressDialog == null) {
                progressDialog = new Dialog(ActivityCropImagePicker.this);
                progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.fragment_progress_dialog);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // Disable Back key and Search key
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_BACK:
                            case KeyEvent.KEYCODE_SEARCH:
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                ProgressBar progressBar = (ProgressBar) progressDialog.findViewById(R.id.progress);
                if (progressBar != null && progressBar.getProgressDrawable() != null)
                    progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                else if (progressBar != null && progressBar.getIndeterminateDrawable() != null)
                    progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
                            PorterDuff.Mode.SRC_IN);
            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void bindViews() {
        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        findViewById(R.id.buttonPickImage).setOnClickListener(btnListener);
        findViewById(R.id.buttonCaptureImage).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);

        findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        findViewById(R.id.button1_1).setOnClickListener(btnListener);
        findViewById(R.id.button4_3).setOnClickListener(btnListener);
        findViewById(R.id.button16_9).setOnClickListener(btnListener);
        findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
    }

    public void cropImage() {
        showProgress();
        mCropView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    mCropView.startLoad(result.getData(), mLoadCallback);
                    isSelectedImage = true;
                    mCropView.setEnabled(true);
                    break;
                case REQUEST_TAKE_IMAGE:
                    if (cameraFileUri != null) {
                        mCropView.startLoad(cameraFileUri, mLoadCallback);
                        isSelectedImage = true;
                        mCropView.setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
    }

    // public void pickImage() {
    // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
    // startActivityForResult(new
    // Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
    // REQUEST_PICK_IMAGE);
    // } else {
    // Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    // intent.addCategory(Intent.CATEGORY_OPENABLE);
    // intent.setType("image/*");
    // startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE);
    // }
    // }

    public Uri createSaveUri() {
        return Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
    }

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.buttonPickImage) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_PICK_IMAGE);
                // showDialogSelectImage();
                // pickImage();
            } else if (v.getId() == R.id.buttonCaptureImage) {
                final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                File newdir = new File(dir);
                newdir.mkdirs();
                String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                }
                cameraFileUri = Uri.fromFile(newfile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                startActivityForResult(cameraIntent, REQUEST_TAKE_IMAGE);
            } else if (v.getId() == R.id.buttonDone) {
                if (isSelectedImage)
                    cropImage();
                else
                    Toast.makeText(getApplicationContext(), "لطفا یک تصویر را انتخاب کنید.", Toast.LENGTH_SHORT).show();
            } else if (v.getId() == R.id.buttonRotateLeft) {
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
            } else if (v.getId() == R.id.buttonRotateRight) {
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            } else if (v.getId() == R.id.buttonFitImage) {
                mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
            } else if (v.getId() == R.id.button1_1) {
                mCropView.setCropMode(CropImageView.CropMode.SQUARE);
            } else if (v.getId() == R.id.button4_3) {
                mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
            } else if (v.getId() == R.id.button16_9) {
                mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
            } else if (v.getId() == R.id.buttonFree) {
                mCropView.setCropMode(CropImageView.CropMode.FREE);
            } else if (v.getId() == R.id.buttonCircle) {
                mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
            }
        }
    };

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            dismissProgress();
        }

        @Override
        public void onError() {
            dismissProgress();
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
        }

        @Override
        public void onError() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dismissProgress();
                    Toast.makeText(ActivityCropImagePicker.this, "خطا در ویرایش تصویر", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
//            dismissProgress();


        }

        @Override
        public void onError() {
            dismissProgress();
        }
    };


    public void startResultActivity(String pic) {
        if (isFinishing())
            return;
        Intent intent = new Intent();
        intent.putExtra("picturePath", pic);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showDialogSelectImage() {
        final CharSequence[] items = {getString(R.string.choose_from_camera), getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_avatar);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        findViewById(R.id.buttonCaptureImage).performClick();
                        break;
                    case 1:
                        findViewById(R.id.buttonPickImage).performClick();
                        break;
                    case 2:
                        dialog.dismiss();
                        finish();
                        break;
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            finish();
        } else
            super.onBackPressed();
    }
}