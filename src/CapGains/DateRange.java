package CapGains;

public class DateRange {

	private SimpleDate _beginDate = null;
	private SimpleDate _endDate = null;

	public DateRange()
	{
	}
	
	public SimpleDate getBeginDate()
	{
		return _endDate;
	}
	
	public SimpleDate getEndDate()
	{
		return _endDate;
	}
	
	public void setBeginDate(SimpleDate aBeginDate)
	{
		_beginDate = aBeginDate;
	}
	
	public void setEndDate(SimpleDate aEndDate)
	{
		_endDate = aEndDate;
	}

	public String toString()
	{
		if (_endDate == null)
		{
			return _beginDate.toString();
		}
		else
		{
			return "various (" + _beginDate.toString() + ","
					+ _endDate.toString() + ")";
		}
	}
}
