DELETE FROM lot;
DELETE FROM trade;
ALTER TABLE lot ALTER COLUMN lot_id RESTART WITH 0;
ALTER TABLE trade ALTER COLUMN trade_id RESTART WITH 0;
