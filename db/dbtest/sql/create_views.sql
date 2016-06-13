CREATE VIEW AccountList AS
SELECT DISTINCT acct.acct_id "Account ID", investor.name "Investor", broker.name "Broker", acct.name "Account", acct_type.label "Type"
FROM acct, acct_type, broker, investor
WHERE acct.broker_id=broker.broker_id
   AND acct.investor_id=investor.investor_id
   AND acct.acct_type_id=acct_type.acct_type_id
ORDER BY investor.name;

