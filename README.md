# GTTrain
Phase III for the CS 4400 Spring 2016


mysql db:
Username: cs4400_Team_58
Password: QxXNykgB

KNOWN ISSUES:
2016-4-19 18:00: Alter table: 所有foreign key需要on delete cascade, on update cascade.（已解决by aj）
2016-4-23 02:02: 1.page 5：添加一个已经存在的卡号会有exception (已解决 by aj)； 2.如果用户没有保存过任何卡那么将不能进行payment method更新（已解决by cina）
2016-4-24 04:06: 目前只有一个bug 用户想订某些数据库已保存的线路时 并不能搜到(已解决 by cina)。