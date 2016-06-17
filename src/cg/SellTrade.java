package cg;

import java.math.BigDecimal;

/**
 * The SellTrade class models a sell trade. In addition to recording the basic
 * attributes of the trade, this class also records the results of the
 * processing of the cap gains for this trade (in the gainComps vector). </p>
 * 
 * @todo Change the data members of this to protected, and add accessor methods.
 *       (Currently access to these data members is public.)
 */
public class SellTrade extends Trade {

	/**
	 * The computed tax gain for this sell trade.
	 */
	TaxGain taxGain;

	public SellTrade(int tradeId, cg.SimpleDate date, Trade.Type tradeType,
			String ticker, int numShares, float sharePrice, BigDecimal comm,
			Trade.SpecialInstruction instruction, String note) {
		
		super(tradeId, date, tradeType, ticker, numShares, sharePrice, comm,
				instruction, note);
		this.numSharesHeld = new Integer(this.numShares);
	}

	void computeTaxGain() {

		taxGain = new TaxGain(ticker, date);

		for (int j = 0; j < lotSet.getNumLots(); j++) {
			OldLot sLot = lotSet.getLotAt(j);

			taxGain.addGainComp(sLot.buyDate, date, sLot.buyPrice, sLot.sellPrice,
					sLot.numShares, sLot.basis, sLot.proceeds, sLot.proceeds - sLot.basis);
		} // end for
	}

	float getProceeds() {
		return ((float) numShares) * ((float) sharePrice) - comm.floatValue();
	}

	float getProceeds(int numShares) {
		return ((float) numShares) / ((float) this.numShares) * getProceeds();
	}

	/**
	 * Get taxGain
	 */
	public TaxGain getTaxGain() {
		return taxGain;
	}

	/**
	 * Re-initialize taxGain
	 */
	void resetTaxGain() {
		if (null != taxGain) {
			taxGain.reset();
		}
	}

	public boolean isOpenPosition() {
		return false;
/*		
		if (numSharesHeld != numShares ) {
			return true;
		} else {
			return false;
		}
*/		
	}
	
	public String toString() {
		if (taxGain != null)
			return (super.toString() + ",Total Gain = " + taxGain
					.getGain());
		else
			return (super.toString());
	}
}
