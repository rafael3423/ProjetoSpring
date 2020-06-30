create table comentario(
id bigint not null auto_increment,
ordemservico_id bigint not null,
descricao text not null,
data_envio datetime not null,
primary key (id)
);

alter table comentario add constraint fk_comentario_ordemservico
foreign key (ordemservico_id) references ordemservico (id);
