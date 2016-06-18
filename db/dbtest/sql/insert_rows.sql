INSERT INTO investor (name) VALUES ('Me');
INSERT INTO broker (name) VALUES ('Brkr');
INSERT INTO acct_type (acct_type_id,label) VALUES (0,'NTx');
INSERT INTO acct_type (acct_type_id,label) VALUES (1,'Tx');
INSERT INTO acct (name,acct_type_id,investor_id,broker_id) VALUES ('TestAccount',0,0,0);
commit;
