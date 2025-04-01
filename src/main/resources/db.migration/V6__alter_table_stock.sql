alter table stock
    change column movementType movement_type varchar(30),
    change column currencyType currency_type varchar(30),
    change column currencyPrice currency_price double,
    change column purchasePrice purchase_price double,
    change column movementDate movement_date datetime;