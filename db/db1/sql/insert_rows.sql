INSERT INTO investor VALUES ('Bill');
INSERT INTO investor VALUES ('Dad');
INSERT INTO investor VALUES ('Karen');
INSERT INTO broker VALUES ('E*Trade');
INSERT INTO broker VALUES ('TD Ameritrade');
INSERT INTO broker VALUES ('Vanguard');
INSERT INTO acct_type VALUES ('Normal');
INSERT INTO acct_type VALUES ('Rollover IRA');
INSERT INTO acct_type VALUES ('Roth IRA');
INSERT INTO acct_type VALUES ('401k');
INSERT INTO acct VALUES ('Main(ET)','My E*Trade Main Account','Normal','Bill','E*Trade');
INSERT INTO acct VALUES ('Roth(ET)','My E*Trade Roth IRA','Roth IRA','Bill','E*Trade');
INSERT INTO acct VALUES ('Roll(ET)','My E*Trade Rollover IRA','Rollover IRA','Bill','E*Trade');
INSERT INTO acct VALUES ('Roll(TD)','My TD Ameritrade Rollover IRA','Rollover IRA','Bill','TD Ameritrade');
INSERT INTO acct VALUES ('Dad','Dad''s Account','Normal','Dad','TD Ameritrade');
INSERT INTO acct VALUES ('Karen','Karen''s Account','Normal','Karen','TD Ameritrade');
commit;

