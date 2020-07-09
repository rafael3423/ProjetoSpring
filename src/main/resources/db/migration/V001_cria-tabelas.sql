create table cliente(
id bigint not null auto_increment,
name varchar (60) not null,
email varchar (60) not null,
phone varchar(20) not null,

primary key (id)
);


create table ordemservico (

id bigint not null auto_increment,
cliente_id bigint not null,
descricao text not null,
preco decimal(10,2) not null,
status varchar(20) not null,
data_abertura datetime not null,
data_finalizacao datetime,

primary key (id)
);

alter table ordemservico add constraint fk_ordemservico_cliente
foreign key (cliente_id) references cliente (id);


create table comentario(
id bigint not null auto_increment,
ordemservico_id bigint not null,
descricao text not null,
data_envio datetime not null,
primary key (id)
);

alter table comentario add constraint fk_comentario_ordemservico
foreign key (ordemservico_id) references ordemservico (id);

