create table usuario (

id bigint not null auto_increment,
perfil varchar(20) not null,
email varchar(20),
senha varchar(500),

primary key (id)
);
