create sequence hibernate_sequence start with 1 increment by 1
create table book (id bigint not null, filename varchar(512), primary key (id))
create table book_categories (book_id bigint not null, categories_id bigint not null)
create table catalog (id bigint not null, name varchar(256), primary key (id))
create table catalog_parents (catalog_id bigint not null, parents_id bigint not null)
create table category (id bigint not null, name varchar(256), catalog_id bigint, primary key (id))
alter table catalog_parents add constraint UK_3hn1ov8gs682u85mubflkc67x unique (parents_id)
alter table book_categories add constraint FKcua8hutsmi5x4wx20sdlptj9w foreign key (categories_id) references category
alter table book_categories add constraint FKrq5mftm1ejl023epqbn42lpa3 foreign key (book_id) references book
alter table catalog_parents add constraint FKfba7asbpxfory2u6h2w9anaby foreign key (parents_id) references category
alter table catalog_parents add constraint FK5pj644s7bjlhgj4l5bku54oo4 foreign key (catalog_id) references catalog
alter table category add constraint FK5wdwj8exshk4qeqk7puil67o6 foreign key (catalog_id) references catalog