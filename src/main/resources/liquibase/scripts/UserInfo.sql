-- liquibase formatted sql

-- changeset anton:1
create table user_info
(
    user_name     varchar(255) NOT NULL UNIQUE,
    role          varchar(250),

    video_hash    varchar(255),
    primary key (user_name),

    foreign key (video_hash) references video(video_hash)
);