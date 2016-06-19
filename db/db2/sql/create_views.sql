CREATE VIEW Accounts (investor, broker, account, label) AS
   SELECT DISTINCT investor.name, broker.name, acct.name, acct_type.label
   FROM acct, acct_type, broker, investor
   WHERE acct.broker_id=broker.broker_id
     AND acct.investor_id=investor.investor_id
     AND acct.acct_type_id=acct_type.acct_type_id
   ORDER BY investor.name, broker.name;
CREATE VIEW OpenPositions as
   SELECT DISTINCT acct.acct_id "AcctID", trade.ticker "Symbol", lot.num_shares "Shares"
   FROM acct, trade, lot
   WHERE lot.trigger_trade_id=trade.trade_id
     AND trade.acct_id=acct.acct_id
     AND lot.has_children=false
     AND lot.state='Open'
   ORDER BY trade.ticker, lot.num_shares;
CREATE VIEW LotReport (acct_id, symbol, buy_date, shares, buy_price) as
   SELECT DISTINCT acct.acct_id, trade.ticker, trade.date, lot.num_shares, trade.price
   FROM acct, trade, lot
   WHERE lot.buy_trade_id=trade.trade_id
     AND trade.acct_id=acct.acct_id
     AND lot.has_children=false
     AND lot.state='Open'
   ORDER BY acct.acct_id, trade.ticker, lot.num_shares;
