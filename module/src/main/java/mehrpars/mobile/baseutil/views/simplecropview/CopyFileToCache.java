package mehrpars.mobile.baseutil.views.simplecropview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import mehrpars.mobile.baseutil.common.Utils;

public class CopyFileToCache {

    private File cacheDir;
    private String cache;
    private Context context;

    public CopyFileToCache(Context context) {
        this.context = context;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(context.getExternalCacheDir().getPath() + "/images/profile");
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        setCash(cacheDir.getPath());
    }

    public String Copy(String sourcePath, String desname) {
        try {
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            btmapOptions.inSampleSize = 2;
            btmapOptions.outHeight = Utils.INSTANCE.getCurrentHeight(300, context);
            btmapOptions.outWidth = Utils.INSTANCE.getCurrentWidth(300, context);
            // Bitmap mm = BitmapFactory.decodeFile(sourcePath);
            Bitmap bm = BitmapFactory.decodeFile(sourcePath, btmapOptions);
            Copy(bm, desname);
            // return Utils.getStringBase64(bm);
            return getStringBase64(bm);
        } catch (Exception e) {
            StringBuffer des = new StringBuffer();
            for (int i = 0; i < e.getStackTrace().length; i++) {
                des.append(e.getStackTrace()[i].getClassName() + " - Line: " + e.getStackTrace()[i].getLineNumber() + " end.");
            }
            return null;
        }
    }

    public static String getStringBase64(Bitmap img) {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, e);
            byte[] byteArray = e.toByteArray();
            return Base64.encodeToString(byteArray, 2);
        } catch (Exception var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public void Copy(Bitmap source, String desname) {
        try {
            File des = this.getFile(desname);
            FileOutputStream out = new FileOutputStream(des);
            source.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            StringBuffer des = new StringBuffer();
            for (int i = 0; i < e.getStackTrace().length; i++) {
                des.append(e.getStackTrace()[i].getClassName() + " - Line: " + e.getStackTrace()[i].getLineNumber() + " end.");
            }
        }
    }


    public File getFile(String url) {
        File f = new File(cacheDir, url);
        return f;
    }

    public String getCash() {
        return cache;
    }

    private void setCash(String cash) {
        this.cache = cash;
    }

}
