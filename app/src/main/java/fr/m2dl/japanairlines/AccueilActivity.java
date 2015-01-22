package fr.m2dl.japanairlines;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class AccueilActivity extends Activity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.menu_principal);
		context = this;
		Button boutonAleatoire = new Button(this);
		Button boutonEditeur = new Button(this);
		boutonAleatoire = (Button) findViewById(R.id.buttonAleatoire);
		boutonEditeur = (Button) findViewById(R.id.buttonEditeur);

		

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                MainActivity.edited = false;
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		};

		boutonAleatoire.setOnClickListener(listener);

		OnClickListener listener2 = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, EditorActivity.class);
				startActivity(intent);
			}

		};

		boutonEditeur.setOnClickListener(listener2);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}