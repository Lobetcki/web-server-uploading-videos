-- liquibase formatted sql

-- changeset anton:2
create table Video
(
    video_hash     varchar(255) NOT NULL,
    name_video     varchar(255) NOT NULL,
    start_time     date,
    endTime        date,
    status         varchar(255),

    user_name     varchar(255) NOT NULL,
    primary key (video_hash),
    foreign key (user_name) references UserInfo(user_name)
);

