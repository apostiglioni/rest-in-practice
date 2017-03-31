CREATE TABLE message
( id           CHAR(32)     NOT NULL
, body         VARCHAR(255) NOT NULL
, publish_time TIMESTAMP    NOT NULL
, author_id    CHAR(32)     NOT NULL
, PRIMARY KEY (id));

CREATE TABLE message_replies
( message_id CHAR(32) NOT NULL
, replies_id CHAR(32) NOT NULL);

CREATE TABLE user
( id       CHAR(32)     NOT NULL
, email    VARCHAR(255) NOT NULL
, username VARCHAR(255) NOT NULL
, PRIMARY KEY (id));

CREATE TABLE user_followers
( user_id      CHAR(32) NOT NULL
, followers_id CHAR(32) NOT NULL
, PRIMARY KEY (user_id, followers_id));

ALTER TABLE message_replies ADD CONSTRAINT UK_e3tri1xg3ayne673tpq2uf9sb UNIQUE (replies_id);
ALTER TABLE user ADD CONSTRAINT UK_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);
ALTER TABLE user ADD CONSTRAINT UK_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);
ALTER TABLE message ADD CONSTRAINT FKfsv5sprbvit5f7c23v6wjxe35 FOREIGN KEY (author_id) REFERENCES user;
ALTER TABLE message_replies ADD CONSTRAINT FK2e0ivnpwj8lhi7blbw7lwhyyu FOREIGN KEY (replies_id) REFERENCES message;
ALTER TABLE message_replies ADD CONSTRAINT FKkgiqq9f3p8hehqv6d3375vskr FOREIGN KEY (message_id) REFERENCES message;
ALTER TABLE user_followers ADD CONSTRAINT FKpvkdr9tjpc96kdwe7591oixnj FOREIGN KEY (followers_id) REFERENCES user;
ALTER TABLE user_followers ADD CONSTRAINT FKokc5w6fibhnthvwnxjxyrlfc1 FOREIGN KEY (user_id) REFERENCES user;
