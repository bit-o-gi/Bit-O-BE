create table if not exists bit_o_user
(
    id           bigint auto_increment
        primary key,
    create_dt    datetime(6)                       null,
    update_dt    datetime(6)                       null,
    connected_dt datetime(6)                       null,
    email        varchar(255)                      null,
    nick_name    varchar(255)                      null,
    platform     enum ('GOOGLE', 'KAKAO', 'NAVER') null,
    provider_id  bigint                            null,
    constraint UK9dt0v20bvw44hm1n96blvk8yf
        unique (email)
);

create table if not exists anniversary
(
    id               bigint auto_increment
        primary key,
    anniversary_date datetime(6)  null,
    content          varchar(255) null,
    title            varchar(255) null,
    update_time      varchar(255) null,
    write_time       varchar(255) null,
    with_people_id   bigint       null,
    writer_id        bigint       null,
    constraint FKa5atrt1jv9hv2v8e41ku168kx
        foreign key (with_people_id) references bit_o_user (id),
    constraint FKn4rpp8gp5a5snbtqa4m21bhwe
        foreign key (writer_id) references bit_o_user (id)
);

create table if not exists couple
(
    id                bigint auto_increment
        primary key,
    create_dt         datetime(6)                               null,
    update_dt         datetime(6)                               null,
    status            enum ('APPROVED', 'CREATING', 'DELETING') null,
    initiator_user_id bigint                                    not null,
    partner_user_id   bigint                                    not null,
    constraint UK4t23lruftm7e5ju164n920hip
        unique (initiator_user_id),
    constraint UKjexxqrbce4f4aprm3mt3ub31x
        unique (partner_user_id),
    constraint FK3418ig1oj2umsr75gxm32on2k
        foreign key (initiator_user_id) references bit_o_user (id),
    constraint FKb8vtn5no8d29cjxs1v5g1jtc2
        foreign key (partner_user_id) references bit_o_user (id)
);

create table if not exists day
(
    id         bigint auto_increment
        primary key,
    create_dt  datetime(6)  null,
    update_dt  datetime(6)  null,
    start_date date         not null,
    title      varchar(255) not null,
    couple_id  bigint       null,
    test       varchar(255) not null,
    constraint UK328i44x7k8h0v6w6x2g1md8vr
        unique (couple_id),
    constraint FK7ustpch632l8mrno37d547jid
        foreign key (couple_id) references couple (id)
);

create table if not exists refresh_token
(
    id            bigint auto_increment
        primary key,
    refresh_token varchar(255) not null,
    user_id       bigint       not null,
    constraint UKf95ixxe7pa48ryn1awmh2evt7
        unique (user_id)
);

create table if not exists schedule
(
    id              bigint auto_increment
        primary key,
    create_dt       datetime(6)  null,
    update_dt       datetime(6)  null,
    content         varchar(255) null,
    end_date_time   datetime(6)  null,
    location        varchar(255) null,
    start_date_time datetime(6)  null,
    title           varchar(255) null,
    user_id         bigint       null,
    constraint FK8hnwi5idvya6ug7g9txfv4o4u
        foreign key (user_id) references bit_o_user (id)
);
