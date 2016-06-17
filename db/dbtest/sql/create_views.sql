CREATE VIEW Accounts AS
   SELECT DISTINCT investor.name "Investor", broker.name "Broker", acct.name "Account", acct_type.label "Type"
   FROM acct, acct_type, broker, investor
   WHERE acct.broker_id=broker.broker_id
     AND acct.investor_id=investor.investor_id
     AND acct.acct_type_id=acct_type.acct_type_id
   ORDER BY investor.name, broker.name;
CREATE VIEW OpenPositions as
   SELECT DISTINCT acct.acct_id "Acct ID", trade.ticker "Symbol", lot.num_shares "Shares"
   FROM acct, trade, lot
   WHERE lot.trigger_trade_id=trade.trade_id
     AND trade.acct_id=acct.acct_id
     AND lot.has_children=false
     AND lot.state='Open'
   ORDER BY trade.ticker, lot.num_shares;
