-- liquibase formatted sql

-- changeset anton:2
create table UserInfo
(
    user_name     varchar(255) NOT NULL,

    downloadable_video varchar(255) NOT NULL
,
    primary key (userName),

    foreign key (Video) references Video(video_hash)
);