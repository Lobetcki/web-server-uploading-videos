-- liquibase formatted sql

-- changeset anton:1
create table UserInfo
(
    user_name     varchar(255) NOT NULL,
    role          varchar(250),

    video_hash    varchar(255),
    primary key (user_name),

    foreign key (video_hash) references video(video_hash)
);