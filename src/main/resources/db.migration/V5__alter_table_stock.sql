alter table stock
change column currencyCoin currencyType varchar(30),
add column currencyPrice Double after currencyType;