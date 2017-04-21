package dev.notify;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddItem extends AppCompatActivity {

    private static final String TAG = "AddItem";
    EditText itemName;
    EditText itemAmount;
    EditText itemExpire;
    ImageButton selectDateExpire;
    EditText itemUser;
    EditText itemNotification;
    ImageButton selectDateNoti;
    Button pickImage;
    Button add;
    ImageView itemImage;

    int year, month, day;
    String user;

    int IMAGE_PICKER = 100;

    private String _rawImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        itemName = (EditText) findViewById(R.id.input_name);
        itemAmount = (EditText) findViewById(R.id.input_amount);
        itemExpire = (EditText) findViewById(R.id.item_expire);
        selectDateExpire = (ImageButton) findViewById(R.id.selectDateExpire);
        itemUser = (EditText) findViewById(R.id.input_users);
        itemNotification = (EditText) findViewById(R.id.notify_expire);
        selectDateNoti = (ImageButton) findViewById(R.id.selectDateNoti);
        pickImage = (Button) findViewById(R.id.pickImage);
        add = (Button) findViewById(R.id.addItem);
        itemImage = (ImageView) findViewById(R.id.imageView);

        SharedPreferences sharedPreferences = getSharedPreferences("notify", Context.MODE_PRIVATE);
        ;
        user = sharedPreferences.getString("username", null);
        itemUser.setText(user);

        selectDateExpire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        selectDateNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNoti();
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void submit() throws ParseException {
        String name = itemName.getText().toString();
        int amount = Integer.parseInt(itemAmount.getText().toString());
        String member = itemUser.getText().toString();
        String expire = itemExpire.getText().toString();
        String notification = itemNotification.getText().toString();

        Boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            itemName.setError("Fill this field");
            focusView = itemName;

            cancel = true;
        } else if (amount == 0) {
            itemAmount.setError("Fill this field");
            ;
            focusView = itemAmount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Date expireDate = new SimpleDateFormat("yyyy-MM-dd").parse(expire);


            Item item = new Item(name, amount, member, _rawImage, expireDate.toString());

            try {
                sentToServer(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void sentToServer(Item item) throws JSONException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("name", item.getName());
        json.put("amount", item.getAmount());
        json.put("expire", item.getExpire());
        json.put("picture", item.getImage());
        json.put("users_username", item.getMember());
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url("https://notify-163706.appspot.com/api/items")
                .post(body)
                .build();
    }

    private void imagePicker() {
        ImagePicker.create(this)
                .imageTitle("Tap to select")
                .single()
                .showCamera(true)
                .start(IMAGE_PICKER);


    }

    private void selectNoti() {
        Calendar calender = Calendar.getInstance();
        NotificationDateFragment dialogFragment = new NotificationDateFragment();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

        dialogFragment.setArguments(args);
        dialogFragment.setCallBack(notificationListener);
        dialogFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    private DatePickerDialog.OnDateSetListener notificationListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectYear, int selectMonth, int selectDayOfMonth) {
            year = selectYear;
            month = selectMonth;
            day = selectDayOfMonth;
            String strDate = "" + day + "-" + month + "-" + year;
            itemNotification.setText(strDate);
        }
    };

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

        date.setArguments(args);
        date.setCallBack(datePickerListener);
        date.show(getSupportFragmentManager(), "Date Picker");
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectYear, int selectMonth, int selectDayOfMonth) {
            year = selectYear;
            month = selectMonth;
            day = selectDayOfMonth;
            String strDate = "" + day + "-" + month + "-" + year;
            itemExpire.setText(strDate);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            String imagePath = images.get(0).getPath();
            Glide.with(getApplicationContext()).load(new File(imagePath)).into(itemImage);
//            BitmapFactory.Options options = new BitmapFactory.Options();

//            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] bytesImage = byteArrayOutputStream.toByteArray();
            _rawImage = Base64.encodeToString(bytesImage, Base64.DEFAULT);

        }
    }
}
