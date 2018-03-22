create table article (
    id bigint not null auto_increment,
    comment_count integer not null,
    content longtext,
    created_at datetime,
    title varchar(1000),
    update_date_time datetime,
    board_id bigint,
    writer_id bigint,
    primary key (id)
);

create table article_tag (
    article_id bigint not null,
    tag_id bigint not null
);

create table board (
    id bigint not null auto_increment,
    list_order integer not null,
    name varchar(50),
    type varchar(255),
    cafe_id bigint,
    primary key (id)
);

create table cafe (
    id bigint not null auto_increment,
    create_date_time datetime,
    description varchar(1000),
    name varchar(100),
    cafe_article_count bigint,
    cafe_comment_count bigint,
    cafe_member_count bigint,
    cafe_visit_count bigint,
    url varchar(30),
    visibility varchar(20) not null,
    category_id bigint,
    primary key (id)
);

create table cafe_member (
    id bigint not null auto_increment,
    join_date datetime,
    role varchar(20),
    cafe_id bigint,
    member_id bigint,
    primary key (id)
);

create table category (
    id bigint not null auto_increment,
    create_date_time datetime,
    list_order integer not null,
    name varchar(255),
    primary key (id)
);

create table comment (
    id bigint not null auto_increment,
    comment varchar(2000),
    create_date_time datetime,
    update_date_time datetime,
    article_id bigint,
    commenter_id bigint,
    primary key (id)
);

create table member (
    id bigint not null auto_increment,
    email varchar(255),
    nickname varchar(30),
    password varchar(255),
    username varchar(255) not null,
    primary key (id)
);

create table tag (
    id bigint not null auto_increment,
    create_date_time datetime,
    name varchar(255),
    primary key (id)
);

alter table cafe
    add constraint UK_cafe_url unique (url);

alter table member
    add constraint UK_member_username unique (username);

alter table tag
    add constraint UK_tag_name unique (name);

alter table article
    add constraint FK_article_board
    foreign key (board_id)
    references board (id);

alter table article
    add constraint FK_article_member
    foreign key (writer_id)
    references member (id);

alter table article_tag
    add constraint FK_article_tag_tag
    foreign key (tag_id)
    references tag (id);

alter table article_tag
    add constraint FK_article_tag_article
    foreign key (article_id)
    references article (id);

alter table board
    add constraint FK_board_cafe
    foreign key (cafe_id)
    references cafe (id);

alter table cafe
    add constraint FK_cafe_category
    foreign key (category_id)
    references category (id);

alter table cafe_member
    add constraint FK_cafe_member_cafe
    foreign key (cafe_id)
    references cafe (id);

alter table cafe_member
    add constraint FK_cafe_member_member
    foreign key (member_id)
    references member (id);

alter table comment
    add constraint FK_comment_article
    foreign key (article_id)
    references article (id);

alter table comment
    add constraint FK_comment_member
    foreign key (commenter_id)
    references member (id);

