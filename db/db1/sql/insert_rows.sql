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
INSERT INTO trade VALUES ('Main(ET)',1,'2000-04-10','Buy','NEOP',3000,1.0,19.95,null);
INSERT INTO trade VALUES ('Main(ET)',2,'2000-04-20','Buy','NEOP',4000,1.5,19.95,null);
INSERT INTO trade VALUES ('Main(ET)',3,'2001-04-15','Sell','NEOP',5000,2.0,19.95,null);
INSERT INTO trade VALUES ('Main(ET)',4,'2001-04-30','Sell','NEOP',2000,3.0,19.95,null);
INSERT INTO trade VALUES ('Dad',1,'2010-04-10','Buy','IFLO',3000,1.0,19.95,null);
INSERT INTO trade VALUES ('Dad',2,'2010-04-20','Buy','IFLO',4000,1.5,19.95,null);
INSERT INTO trade VALUES ('Dad',3,'2011-04-15','Sell','IFLO',5000,2.0,19.95,null);
INSERT INTO trade VALUES ('Dad',4,'2011-04-30','Sell','IFLO',2000,3.0,19.95,null);
