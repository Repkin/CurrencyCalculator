package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView txt, textView3;
    private Spinner spinner;
    private EditText editText;
    private Button button;

    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("currencies.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);
        } catch (XmlPullParserException | IOException ignored) {
        }
    }

    public void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Currency> currencies = new ArrayList<>();
        HashMap<String, ArrayList<Currency>> money = new HashMap<String, ArrayList<Currency>>();
        int eventType = parser.getEventType();
        Currency currentCurrency = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("currency".equals(eltName)) {
                        currentCurrency = new Currency();
                        currencies.add(currentCurrency);
                    } else if (currentCurrency != null) {
                        if ("name".equals(eltName)) {
                            currentCurrency.name = parser.nextText();
                        } else if ("rate".equals(eltName)) {
                            currentCurrency.rate = Double.parseDouble(parser.nextText());
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }
        printCurrencies(currencies);


    }


    private void printCurrencies(ArrayList<Currency> currencies) {
        StringBuilder builder = new StringBuilder();

        for (Currency currency : currencies) {
            builder.append(currency.name).append("\n").append(currency.rate).append("\n\n");
        }
        txt.setText(builder.toString());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.txt);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText = (EditText) findViewById(R.id.editTextNumberDecimal);
        button = (Button) findViewById(R.id.button);
        textView3 = (TextView) findViewById(R.id.textView3);
        parseXML();

    }


}
