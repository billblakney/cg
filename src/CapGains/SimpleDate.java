package CapGains;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class represents a trade date. It extends the GregorianCalendar class to
 * include a date of "various", which is used in some situations, such as the
 * buy date for a lot of shares that were purchased on various dates.
 */
public class SimpleDate extends GregorianCalendar {

	/**
	 * Default constructor which gives today's date.
	 */
	public SimpleDate() {
		clear();
		Calendar now = new GregorianCalendar();
		set(now.get(YEAR),now.get(MONTH),now.get(DAY_OF_MONTH));
	}

	/**
	 * Constructor used to initialize this date with a specific date.
	 * 
	 * @param year
	 *            Year of the date.
	 * @param month
	 *            Month of the date.
	 * @param day
	 *            Day of month of the date.
	 */
	public SimpleDate(int year, int month, int day) {
		clear();
		set(year, month, day);
/*
System.out.println("HOUR: " + get(Calendar.HOUR));
System.out.println("HOUR_OF_DAY: " + get(Calendar.HOUR_OF_DAY));
System.out.println("MINUTE: " + get(Calendar.MINUTE));
System.out.println("HOUR: " + get(Calendar.HOUR));
System.out.println("MILLISECOND: " + get(Calendar.MILLISECOND));
*/
	}

	/**
	 * Constructor used to initialize this date with a specific date.
	 * 
	 * @param date
	 *            Date.
	 */
	public SimpleDate(Date date) {
		setTime(date);
	}
	
	/**
	 * Get the date as a Date instance.
	 * @return
	 */
	public Date getDate()
	{
	   return getTime();
	}

	/**
	 * Returns the year in string format. If this trade date holds an actual
	 * date, the year is returned in yyyy format. Otherwise, the string assigned
	 * to this date is returned.
	 */
	public String getYearString() {
		return getString("yyyy");
	}

	/**
	 * Returns the year.
	 */
	public int getYear() {
		return get(Calendar.YEAR);
	}

	/**
	 * Returns the month.
	 */
	public int getMonth() {
		return get(Calendar.MONTH);
	}

	/**
	 * Returns the month.
	 */
	public int getDayOfMonth() {
	   return get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the month in string format. If this trade date holds an actual
	 * date, the month is returned in MM format. Otherwise, the string assigned
	 * to this date is returned.
	 */
	public String getMonthString() {
		return getString("mm");
	}

	/**
	 * Returns the day of month in string format. If this trade date holds an
	 * actual date, the day of month is returned in MM format. Otherwise, the
	 * string assigned to this date is returned.
	 */
	public String getDayString() {
		return getString("dd");
	}

	/**
	 * Returns a string representation of this date.
	 */
	public String toString() {
		return getString("MM/dd/yy");
	}

	/**
	 * Returns a String representing the date in the requested format.
	 */
	private String getString(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setCalendar(this);
		String dateString = formatter.format(this.getTime());
		return dateString;
	}
}
