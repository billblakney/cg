package CapGains;

import java.text.*;
import java.util.*;

/**
 * This class represents a single record of the cap gains for a sell trade. The
 * record corresponds to an entry that would be entered in a tax filing.
 */
public class TaxGain extends Lot {

	/**
	 * The date of the most recent buy corresponding to this tax gain. The date
	 * of the oldest by is stored in the buyDate field of the base class.
	 */
	private SimpleDate newestBuyDate;

	/**
	 */
	public DateRange buyDateRange = new DateRange();

	/**
	 * The year for which this tax gain should have been reported.
	 * This value is computed by this program. (zzz)
	 */
	public Integer computedTaxYear = new Integer(0);

	/**
	 * The year for which this tax gain was actually reported.
	 * This is a user entered field that can be used to track the abnormal
	 * case where the tax gain was reported too late, presumably by mistake.
	 */
	public Integer claimedTaxYear = new Integer(0);

	/**
	 * The gain components for this trade.
	 */
	protected Vector<Lot> gainComps = new Vector<Lot>();

	/**
	 * Constructor.
	 */
	public TaxGain(String ticker) {
		this.ticker = ticker;
	}

	public TaxGain(String ticker, SimpleDate sellDate) {
		this.ticker = ticker;
		this.sellDate = sellDate;
	}

	/**
	 * Add a gain component to this tax gain. Note that proceeds, basis, gain,
	 * and buyDate of this tax gain will be updated to include the component
	 * values.
	 */
	public void reset() {
		numShares = 0;
		proceeds = (float) 0.0;
		basis = (float) 0.0;
		gain = (float) 0.0;
		gainComps = new Vector<Lot>();
	}

	public float getGain() {
		return gain;
	}

	/**
	 * Adds a GainComponentto this SellTrade. The application code that computes
	 * the cap gains for this sell trade is expected to call this routine one
	 * time for each of the cap gain components that correspond to this
	 * SellTrade. </p> Note on the internal algorithm: A call to this routine
	 * adds the gain component to the gain components vector, plus updates the
	 * total cap gain of this SellTrade.
	 */
	protected void addGainComp(SimpleDate buyDate, SimpleDate sellDate,
			float buyPrice, float sellPrice, long numShares,
			float basis, float proceeds, float componentGain) {

		// gc.numShares += gc.numShares; not used since we initialize with sell
		// trade values for now
		this.numShares += numShares;
		this.proceeds += proceeds;
		this.basis += basis;
		this.gain += componentGain;

		Lot gc = new Lot(ticker, buyDate,sellDate,buyPrice,sellPrice,
				numShares, basis, proceeds, componentGain);
		gainComps.addElement(gc);

		// Set the buy dates for this component.
		// If there are more than one gain components, set buy date to
		// "various".
		if (gainComps.size() == 1) {
			this.buyDate = gc.buyDate;
			this.newestBuyDate = gc.buyDate;
			this.term = gc.term;
			buyDateRange.setBeginDate(gc.buyDate);
		} else {
			// First, update term.
			if (gc.term != this.term)
				this.term = Term.MIXED;

			// Next, update oldestBuyDate and newestBuyDate as appropriate.
			if (gc.buyDate.before(this.buyDate) == true) {
				this.buyDate = gc.buyDate;
				buyDateRange.setBeginDate(gc.buyDate);
			}
			if (gc.buyDate.after(this.newestBuyDate) == true) {
				this.newestBuyDate = gc.buyDate;
				buyDateRange.setEndDate(gc.buyDate);
			}
		}
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		// first build the date string
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		String buyDateString = formatter.format(buyDate);
		String sellDateString = formatter.format(sellDate);

		s.append(numShares).append(",");
		s.append(ticker).append(",");
		s.append(buyDateString).append(",");
		s.append(sellDateString).append(",");
		s.append(proceeds).append(",");
		s.append(basis).append(",");
		s.append(gain);
		s.append(term);
		s.append(computedTaxYear);
		s.append(claimedTaxYear);

		return s.toString();
	}
}
