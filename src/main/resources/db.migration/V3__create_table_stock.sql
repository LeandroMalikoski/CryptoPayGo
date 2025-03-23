create table stock(

                      id bigint auto_increment,
                      product_id bigint not null,
                      user_id bigint not null,
                      quantity integer,
                      movementType enum("ENTRY", "EXIT") not null,
                      movementDate timestamp default current_timestamp,
                      primary key (id),
                      foreign key (product_id) references product(id),
                      foreign key (user_id) references users(id)

);