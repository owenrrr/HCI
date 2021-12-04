USE seiii;

DELETE FROM entity;
INSERT INTO entity (eid, pid, name, type, property) VALUES
("1", 1,"詹姆·斯图尔特", "Character", "{'出生': '英格兰', '血统': '麻瓜', '婚姻状况': '已婚', '物种': '人类', '性别': '男', '职业': ['石匠', '伊尔弗莫尼魔法学校校长及创办者']}"),
("2", 1,"玛莎·斯图尔特", "Character", "{'血统': '麻瓜', '婚姻状况': '已婚', '物种': '人类', '性别': '女性'}"),
("3", 1,"伊索特·塞耶", "Character", "{'出生': '约1603年 伊尔弗莫尼小屋，柯姆洛格拉谷地，凯里郡，爱尔兰 ', '逝世': '晚于1703年年', '血统': '纯血统', '婚姻状况': '已婚', '物种': '人类', '性别': '女', '职业': ['伊尔弗莫尼魔法学校校长及创办者'], '学院': '长角水蛇学院'}");

DELETE FROM relation;
INSERT INTO relation (pid, source_id, target_id, source, target, relation, type, hash_id) VALUES
(1, "1", "2", "詹姆·斯图尔特", "玛莎·斯图尔特", "母亲", "connection", "e0" ),
(1, "1", "3", "詹姆·斯图尔特", "伊索特·塞耶", "父亲", "connection", "e123" );

DELETE FROM position;
INSERT INTO position (id, pid, x, y) VALUES
("1", 1, 269.05429063897554, -856.6604646064388),
("2", 1, 402.14926635710526, 965.6494421468306),
("3", 1, 903.968804722484, 743.0099466160189);

DELETE FROM `project`;
INSERT INTO `project` value (1, 1, '哈利波特');

DELETE FROM `user`;
INSERT INTO `user` value (1, '123456', '123@qq.com');