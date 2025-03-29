alter table stock
add column currencyCoin varchar(30) after movementType,
add column purchasePrice Double after currencyCoin;