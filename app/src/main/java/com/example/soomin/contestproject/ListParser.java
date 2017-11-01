package com.example.soomin.contestproject;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class ListParser extends AsyncTask<ArrayList<String>, Void, ArrayList<ItemVO>> {
    /**
     * @throws Exception
     */
    @Override
    protected ArrayList<ItemVO> doInBackground(ArrayList<String>... search) {
        XmlPullParserFactory factory;
        XmlPullParser parser;
        URL xmlUrl;
        ArrayList<ItemVO> returnResult = new ArrayList<>();
        try {
            String preName = null;
            ItemVO item = new ItemVO();
            xmlUrl = new URL(search[0].get(0));
            System.out.println("dmdmdm" + search[0].get(0));
            xmlUrl.openConnection().getInputStream();
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(xmlUrl.openStream(), "utf-8");
            System.out.println("dmdmdm" + parser);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        preName = parser.getName();
                        if (preName.equalsIgnoreCase("list")) {
                            item = new ItemVO();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("list")) {
                            returnResult.add(item);
                        }
                        preName = null;
                        break;
                    case XmlPullParser.TEXT:
                        System.out.println("3333333333333333" + parser.getText());
                        if (preName == null)
                        {

                        } else if (preName.equalsIgnoreCase("apiSid")) {
                            item.apiSid = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiDongName")) {
                            item.apiDongName = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiNewAddress")) {
                            item.apiNewAddress = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiOldAddress")) {
                            item.apiOldAddress = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiTel")) {
                            item.apiTel = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiLat")) {
                            item.apiLat = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiLng")) {
                            item.apiLng = parser.getText();
                        }
                        else if (preName.equalsIgnoreCase("apiRegDate")) {
                            item.apiRegDate = parser.getText();
                        }

                        break;
                }
                eventType = parser.next();
            }


        } catch (Exception e) {

        }
        return returnResult;
    }

    @Override
    protected void onPostExecute(ArrayList<ItemVO> s) {

    }
}
