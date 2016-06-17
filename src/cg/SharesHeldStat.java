package cg;

public class SharesHeldStat {
	
	public String ticker = null;
	public Term term;
	public Integer num_shares;
	public Float total_cost;
	public Float lo_share_price;
	public Float hi_share_price;
	public Float av_share_price;
	
	public SharesHeldStat(String ticker, Term term, Float init_lo_price, Float init_hi_price){
		this.ticker = ticker;
		this.term = term;
		num_shares = new Integer(0);
		total_cost = new Float(0.0);
		lo_share_price = init_lo_price;
		hi_share_price = init_hi_price;
		av_share_price = new Float(0.0);
	}
}
