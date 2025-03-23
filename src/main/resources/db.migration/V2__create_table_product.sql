create table product(

                        id bigint auto_increment,
                        name varchar(100) not null,
                        description varchar(255) not null,
                        category varchar(100) not null,
                        price double not null,
                        quantity integer,
                        primary key (id)

);