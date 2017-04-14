package rojoxpress.slideexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rojoxpress.slidebutton.SlideButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.progress);
        final SlideButton slideButton = (SlideButton) findViewById(R.id.slide_button);
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_);

        slideButton.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void onSlide() {
                Toast.makeText(MainActivity.this,"UNLOCKED",Toast.LENGTH_SHORT).show();
            }
        });

        slideButton.setOnSlideChangeListener(new SlideButton.OnSlideChangeListener() {
            @Override
            public void onSlideChange(float position) {
                textView.setText("Progress: "+position);
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                slideButton.setEnabled(b);
            }
        });
    }
}
