package name.chakimar.deletecachetest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private WebView webview;
	private Button btnShowCacheSize;
	private Button btnCreateCache;
	private Button btnDeleteCache;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		webview = (WebView) findViewById(R.id.webview);
		webview.setWebViewClient(new WebViewClient());
		webview.setWebChromeClient(new WebChromeClient());

		btnShowCacheSize = (Button) findViewById(R.id.btn_show_cache_size);
		btnCreateCache = (Button) findViewById(R.id.btn_create_cache);
		btnDeleteCache = (Button) findViewById(R.id.btn_delete_cache);

		btnShowCacheSize.setOnClickListener(this);
		btnCreateCache.setOnClickListener(this);
		btnDeleteCache.setOnClickListener(this);

		loadData();
	}

	private void loadData() {
		webview.loadUrl("http://www.yahoo.co.jp");
	}

	void createCache(Context context) {
		loadData();

		try {
			createTmpFile(context);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createTmpFile(Context context) throws IOException {
		FileWriter fw = null;
		try {
			File dir = new File(context.getCacheDir(), "dir1");
			dir.mkdir();
			File f = new File(dir, "test.txt");
			fw = new FileWriter(f);
			fw.write("test data");
		} finally {
			if (fw != null) fw.close();
		}
	}

	void deleteCacheDir(Context context) {
		Runtime localRuntime = Runtime.getRuntime();

		String cachePath = context.getCacheDir().getPath();
		String cmd = "rm -R " + cachePath;
		try {
			localRuntime.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		if (v == btnShowCacheSize) {
			showCacheSize(this);
		} else if (v == btnCreateCache) {
			createCache(this);
		} else if (v == btnDeleteCache) {
			deleteCacheDir(this);
		}
	}

	private void showCacheSize(Context context) {
		String msg = "cache size = " + calcFileSize(context.getCacheDir());
		showToast(msg);
	}

	public static int calcFileSize(File f) {
		if (f == null) {
			return 0;
		}
		int size = 0;

		if (f.isFile()) {
			size = (int) f.length();
		} else if (f.isDirectory()) {
			for (File f2 : f.listFiles()) {
				size += calcFileSize(f2);
			}
		}
		return size;
	}
}