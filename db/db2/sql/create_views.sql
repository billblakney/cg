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
---------------------------------
CREATE VIEW vwOpenPositions AS
   SELECT acct.acct_id   acct_id,
          fbt.ticker     symbol,
          lot.num_shares shares,
          fbt.seqnum     acquire_seqnum,
          fbt.date       basis_date,
          lbt.date       buy_date,
          lot.basis      basis
   FROM lot lot
     INNER JOIN trade fbt
       ON lot.first_buy_trade_id = fbt.trade_id
     INNER JOIN trade lbt
       ON lot.last_buy_trade_id = lbt.trade_id
     INNER JOIN acct acct
       ON acct.acct_id = fbt.acct_id
   WHERE lot.state='Open'
   ORDER BY acct_id, symbol, acquire_seqnum;
---------------------------------
-- TODO look at last_buy vs acquire issue
CREATE VIEW ClosedLotReport AS
  SELECT a.acct_id    acct_id,
         l.lot_id     lot_id,
         bt.ticker    symbol,
         l.num_shares num_shares,
         bt.date      buy_date,
         bt.price     buy_price,
         st.date      sell_date,
         st.price     sell_price,
         l.basis      basis,
         l.proceeds   proceeds
   FROM lot l
     INNER JOIN trade bt
       ON l.last_buy_trade_id = bt.trade_id
     INNER JOIN trade st
       ON l.sell_trade_id = st.trade_id
     INNER JOIN acct a
       ON a.acct_id = st.acct_id
    WHERE l.state = 'Closed';
