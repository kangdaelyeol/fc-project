insert into user_account (user_id, user_password, nickname, email, memo, created_at, created_by,
                          modified_at, modified_by)
values ('uno', 'asdf1234', 'Uno', 'uno@mail.com', 'I am Uno.', now(), 'uno', now(), 'uno')
;

insert into article (user_account_id, title, content, hashtag, created_by, modified_by, created_at,
                     modified_at)
values (1, 'Quisque ut erat.',
        'Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Du.',
        '#pink', 'Kamilah', 'Murial', '2021-05-30 23:53:46', '2021-03-10 08:48:50');



insert into article_comment (article_id, user_account_id, content, created_at, modified_at,
                             created_by, modified_by)
values (1, 1,
        'Quisque id jus eros.',
        '2021-03-02 22:40:04', '2021-04-27 15:38:09', 'Lind', 'Orv'),
       (1, 1,
        'Quisque id j posuere cubilia Curae; Nulla dapibus dolor vel est. Donec odio os.',
        '2021-06-08 04:36:02', '2022-01-25 15:35:42', 'Trstram', 'Loy');
