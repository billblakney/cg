create text table investor (
   investor_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
   name varchar(50) NOT NULL);
create text table broker (
   broker_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
   name varchar(20) NOT NULL);
create text table acct_type (
   acct_type_id INT PRIMARY KEY,
   is_taxable boolean NOT NULL,
   label varchar(50) NOT NULL);
create text table acct (
   acct_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
   name varchar(20) NOT NULL,
   acct_type_id INT NOT NULL,
   investor_id INT NOT NULL,
   broker_id INT NOT NULL,
   FOREIGN KEY (acct_type_id) REFERENCES acct_type(acct_type_id),
   FOREIGN KEY (investor_id) REFERENCES investor(investor_id),
   FOREIGN KEY (broker_id) REFERENCES broker(broker_id));
create text table trade (
   trade_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
   acct_id INT NOT NULL,
   seqnum INT NOT NULL,
   date DATE NOT NULL,
   buysell varchar(4) NOT NULL,
   ticker varchar(6) NOT NULL,
   shares INT NOT NULL,
   price REAL NOT NULL,
   commission REAL NOT NULL,
   special_rule varchar(10),
   FOREIGN KEY (acct_id) REFERENCES acct(acct_id));
create text table lot (
   lot_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
   parent_id INT,
   has_children BOOLEAN,
   trigger_trade_id INT,
   acquire_trade_id INT NOT NULL,
   last_buy_trade_id INT NOT NULL,
   last_sell_trade_id INT,
   num_shares INT NOT NULL,
   basis FLOAT NOT NULL,
   proceeds FLOAT,
   state varchar(20) NOT NULL,
   close_date DATE,
   FOREIGN KEY (trigger_trade_id) REFERENCES trade(trade_id),
   FOREIGN KEY (acquire_trade_id) REFERENCES trade(trade_id),
   FOREIGN KEY (last_buy_trade_id) REFERENCES trade(trade_id),
   FOREIGN KEY (last_sell_trade_id) REFERENCES trade(trade_id));
