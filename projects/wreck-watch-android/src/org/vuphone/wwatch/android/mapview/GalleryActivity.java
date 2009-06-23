package org.vuphone.wwatch.android.mapview;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.VUphone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
		VUphone.setContext(this);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gallery);

	    String point = getIntent().
	    	getStringExtra("org.vuphone.wwatch.android.mapping.GalleryActivity.point");
	    Log.d(VUphone.tag, "GalleryActivity: The point is "+point);
	    Gallery g = (Gallery) findViewById(R.id.gallery);
	    
	    g.setAdapter(new ImageAdapter(this, -1));

	    g.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            //Toast.makeText(GalleryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}


}
