create table user(
email varchar (30) not null,
userId MEDIUMINT not null AUTO_INCREMENT primary key,
password varchar (20) not null,
gender Character(1),
avatar VarChar(50),
name Character(40),
nickName Character(40)
);

create table course(
courseId MEDIUMINT not null primary key AUTO_INCREMENT,
courseName VarChar(40) not null,
courseIntroduce VarChar(500) not null,
courseStartTime date not null,
courseEndTime date not null,
picture VarChar(50)
);

create table project(
projectId MEDIUMINT not null primary key AUTO_INCREMENT,
projectName VarChar(40) not null,
projectIntroduce Varchar(500) not null,
projectStartTime date not null,
projectEndTime date not null,
teacherProportion TINYINT default 100,
selfProportion TINYINT default 0,
mutualProportion TINYINT default 0
);

create table task(
taskId MEDIUMINT not null primary key AUTO_INCREMENT,
taskName VarChar(40) not null,
taskIntroduce Varchar(500) not null,
taskStartTime date not null,
taskEndTime date not null,
importance TINYINT default 3
);

create table reply(
replyId MEDIUMINT not null primary key AUTO_INCREMENT,
replyContent VarChar(500) not null ,
replyTime datetime not null
);

create table post(
postId MEDIUMINT not null primary key AUTO_INCREMENT,
postName VarChar(40) not null,
postContent VarChar(500) not null,
postTime datetime not null ,
likesCount TINYINT default 0
);

create table file(
fileId MEDIUMINT not null primary key AUTO_INCREMENT,
fileName VarChar(40) not null,
path VarChar(50) not null,
uploadTime datetime not null
);

create table take(
userId MEDIUMINT not null,
courseId MEDIUMINT not null,
primary key (userId,courseId),
foreign key (userId) references user(userId),
foreign key (courseId) references course(courseId)
);

create table teach(
userId MEDIUMINT not null,
courseId MEDIUMINT not null,
primary key (userId,courseId),
foreign key (userId) references user(userId),
foreign key (courseId) references course(courseId)
);

create table cpInclusion(
courseId MEDIUMINT not null,
projectId MEDIUMINT not null,
primary key (courseId,projectId),
foreign key (projectId) references project(projectId),
foreign key (courseId) references course(courseId)
);

create table ptInclusion(
taskId MEDIUMINT not null,
projectId MEDIUMINT not null,
primary key (taskId,projectId),
foreign key (projectId) references project(projectId),
foreign key (taskId) references task(taskId)
);

create table participate(
userId MEDIUMINT not null,
projectId MEDIUMINT not null,
teacherGrade TINYINT not null,
selfGrade TINYINT not null,
mutualGrade TINYINT not null,
isGroupLeader tinyint(1) not null,
primary key (userId,projectId),
foreign key (projectId) references project(projectId),
foreign key (userId) references user(userId)
);

create table evaluate(
evaluateUserId MEDIUMINT not null,
evaluatedUserId MEDIUMINT not null,
projectId MEDIUMINT not null,
primary key (evaluateUserId,evaluatedUserId,projectId),
foreign key (projectId) references project(projectId),
foreign key (evaluateUserId) references user(userId),
foreign key (evaluatedUserId) references user(userId)
);

create table supervise(
superviseUserId MEDIUMINT not null,
supervisedUserId MEDIUMINT not null,
taskId MEDIUMINT not null,
primary key (superviseUserId,supervisedUserId,taskId),
foreign key (taskId) references task(taskId),
foreign key (superviseUserId) references user(userId),
foreign key (supervisedUserId) references user(userId)
);

create table selectTask(
userId MEDIUMINT not null,
taskId MEDIUMINT not null,
isAccomplished tinyint(1) not null,
primary key (userId,taskId),
foreign key (taskId) references task(taskId),
foreign key (userId) references user(userId)
);

create table upload(
projectId MEDIUMINT not null,
userId MEDIUMINT not null,
fileId MEDIUMINT not null,
primary key (projectId,userId,fileId),
foreign key (projectId) references project(projectId),
foreign key (userId) references user(userId),
foreign key (fileId) references file(fileId)
);

create table userReply(
projectId MEDIUMINT not null,
userId MEDIUMINT not null,
replyId MEDIUMINT not null,
primary key (projectId,userId,replyId),
foreign key (projectId) references project(projectId),
foreign key (userId) references user(userId),
foreign key (replyId) references reply(replyId)
);

create table userPost(
projectId MEDIUMINT not null,
userId MEDIUMINT not null,
postId MEDIUMINT not null,
primary key (projectId,userId,postId),
foreign key (projectId) references project(projectId),
foreign key (userId) references user(userId),
foreign key (postId) references post(postId)
);

create table likes(
userId MEDIUMINT not null,
postId MEDIUMINT not null,
primary key (userId,postId),
foreign key (userId) references user(userId),
foreign key (postId) references post(postId)
);

