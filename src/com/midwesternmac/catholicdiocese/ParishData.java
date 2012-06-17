package com.midwesternmac.catholicdiocese;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import au.com.bytecode.opencsv.CSVReader;

import com.midwesternmac.catholicdiocese.Parish;

public class ParishData extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String PARISH_DATA_TABLE_NAME = "parishes";
	private Context _context;

	public ParishData(Context context) {
		super(context, PARISH_DATA_TABLE_NAME, null, DATABASE_VERSION);
		_context = context; // Save context for use in onCreate.
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create the parish data table.
		String sql = "CREATE TABLE IF NOT EXISTS " + PARISH_DATA_TABLE_NAME + " (" +
						BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"parish_id TEXT, name TEXT, " +
						"street_address TEXT, city TEXT, state TEXT, zip_code TEXT, " +
						"phone_number TEXT, fax_number TEXT, " +
						"website_url TEXT, " +
						"type TEXT, " +
						"latitude REAL, longitude REAL)";
		database.execSQL(sql);

		// Import parish data from locations CSV file in assets folder.
		CSVReader reader;
		try {
			InputStream inputStream = _context.getAssets().open("locations.csv");
			Reader fileReader = new InputStreamReader(inputStream);
			reader = new CSVReader(fileReader);
			String[] nextLine;
			try {
				while ((nextLine = reader.readNext()) != null) {
					// nextLine[] is an array of values from the current CSV line.
					ContentValues values = new ContentValues();
					values.put("parish_id", nextLine[0]);
					values.put("name", nextLine[1]);
					values.put("street_address", nextLine[2]);
					values.put("city", nextLine[3]);
					values.put("state", nextLine[4]);
					values.put("zip_code", nextLine[5]);
					values.put("phone_number", nextLine[10]);
					// Only add fax number if it's in the CSV line.
					if (nextLine.length == 12) {
						values.put("fax_number", nextLine[11]);
					}
					// Only add website URL if it's not empty.
					if (nextLine[9].length() > 0) {
						values.put("website_url", nextLine[9]);
					}
					values.put("type", nextLine[8]);
					values.put("latitude", nextLine[6]);
					values.put("longitude", nextLine[7]);
					database.insert(PARISH_DATA_TABLE_NAME, "parish_id", values);
				}
			} catch (IOException e) {
				// If the reader can't read a line, that stinks for us!
				e.printStackTrace();
			}
		} catch (IOException e) {
			// If the file's not in the assets folder, we're up a creek!
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// Once there is a schema change, increment the DATABASE VERSION, and
		// any statements inside this method will be executed.
	}

	public Parish getParishByID(String id) {
		Parish parish = new Parish(null);

		// Connect to the database and retrieve the parish.
		// TODO: Use a better/more secure way to query the db with placeholders.
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM " + PARISH_DATA_TABLE_NAME + " WHERE parish_id = '" + id + '\'', null);
		cursor.moveToFirst();

		// Build the parish object from the row.
		parish.setParishID(cursor.getString(cursor.getColumnIndex("parish_id")));
		parish.setName(cursor.getString(cursor.getColumnIndex("name")));
		parish.setStreetAddress(cursor.getString(cursor.getColumnIndex("street_address")));
		parish.setCity(cursor.getString(cursor.getColumnIndex("city")));
		parish.setState(cursor.getString(cursor.getColumnIndex("state")));
		parish.setZipCode(cursor.getString(cursor.getColumnIndex("zip_code")));
		parish.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone_number")));
		// Not all entries have a Fax Number.
		if (cursor.getString(cursor.getColumnIndex("fax_number")) != null) {
			parish.setFaxNumber(cursor.getString(cursor.getColumnIndex("fax_number")));
		}
		// Not all entries have a Website URL.
		if (cursor.getString(cursor.getColumnIndex("website_url")) != null) {
			parish.setWebsiteURL(cursor.getString(cursor.getColumnIndex("website_url")));
		}
		parish.setType(cursor.getString(cursor.getColumnIndex("type")));
		parish.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
		parish.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));

		return parish;
	}

	public List<Parish> getAllParishes() {
		List<Parish> parishes = new ArrayList<Parish>();

		// Connect to the database and retrieve all rows.
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM " + PARISH_DATA_TABLE_NAME, null);

		// Make sure we're starting on the first record.
		cursor.moveToFirst();

		// Loop through rows, and add each parish record to the parishes list.
		while (cursor.isAfterLast() == false) {
			Parish currentParish = new Parish(null);
			currentParish.setParishID(cursor.getString(cursor.getColumnIndex("parish_id")));
			currentParish.setName(cursor.getString(cursor.getColumnIndex("name")));
			currentParish.setStreetAddress(cursor.getString(cursor.getColumnIndex("street_address")));
			currentParish.setCity(cursor.getString(cursor.getColumnIndex("city")));
			currentParish.setState(cursor.getString(cursor.getColumnIndex("state")));
			currentParish.setZipCode(cursor.getString(cursor.getColumnIndex("zip_code")));
			currentParish.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone_number")));
			// Not all entries have a Fax Number.
			if (cursor.getString(cursor.getColumnIndex("fax_number")) != null) {
				currentParish.setFaxNumber(cursor.getString(cursor.getColumnIndex("fax_number")));
			}
			// Not all entries have a Website URL.
			if (cursor.getString(cursor.getColumnIndex("website_url")) != null) {
				currentParish.setWebsiteURL(cursor.getString(cursor.getColumnIndex("website_url")));
			}
			currentParish.setType(cursor.getString(cursor.getColumnIndex("type")));
			currentParish.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
			currentParish.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));

			// Add the current record to the parishes list.
			parishes.add(currentParish);

			// After saving a parish, move on to the next row.
			cursor.moveToNext();
		}

		// Close the database connection and return the list.
		database.close();
		return parishes;
	}
}
