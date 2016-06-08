INSERT INTO investor (name) VALUES ('Me');
INSERT INTO broker (name) VALUES ('Brkr');
INSERT INTO acct_type (acct_type_id,label) VALUES (0,'TX');
INSERT INTO acct_type (acct_type_id,label) VALUES (1,'nTX');
INSERT INTO acct (name,acct_type_id,investor_id,broker_id) VALUES ('tstacct',0,0,0);
commit;
