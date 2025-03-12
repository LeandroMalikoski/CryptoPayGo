create table users(

                      id bigint not null auto_increment,
                      name varchar(100) not null,
                      email varchar(255) not null,
                      password varchar(100) not null,
                      role varchar(50) not null,

                      primary key (id)

);