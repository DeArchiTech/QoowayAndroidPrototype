package ui;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory.Options;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import framework.ImageDownloader.AuthImageDownloader;

public class UILApplication extends Application {
        @Override
        public void onCreate() {


                super.onCreate();

                initImageLoader(getApplicationContext());
        }

        public static void initImageLoader(Context context) {

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
            .threadPriority(Thread.NORM_PRIORITY - 2)
            .denyCacheImageMultipleSizesInMemory()
            .discCacheFileNameGenerator(new Md5FileNameGenerator())
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .writeDebugLogs() // Remove for release app
			.memoryCacheSize(41943040)
			.discCacheSize(104857600)
			.threadPoolSize(10)
			.imageDownloader(new AuthImageDownloader(context))
            .build();
// Initialize ImageLoader with configuration.

ImageLoader.getInstance().init(config);
        }
}