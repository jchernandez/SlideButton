package rojoxpress.slideexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rojoxpress.slidebutton.SlideButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlideButton slideButton = (SlideButton) findViewById(R.id.slide_button);

        slideButton.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void handleSlide() {
                Toast.makeText(MainActivity.this,"UNLOCKED",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
