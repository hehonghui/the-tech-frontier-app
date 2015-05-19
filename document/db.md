# 数据库结构
----

数据库名 : `tech_frontier_app.db`;

**articles-表**

|   字段名  |   类型   |   备注    | 
|----------|----------|----------|
|   aid   |    int    |   主键，文章id |
|   author   |   varchar(30)  |   作者名 |
|   title   |   varchar(100)   |   文章标题 |
|   atype   |   int   |   文章分类,2为android,3为iOS |
|   save_time   |    date    |   这篇文章存储到数据库的时间 |


**favorites-文章收藏表**

|   字段名  |   类型   |   备注    | 
|----------|----------|----------|
|   id   |    int    |   主键，自增 |
|   aid   |    int    |   文章id |
|   uid   |    int    |   用户id |

**users-用户表**

|   字段名  |   类型   |   备注    | 
|----------|----------|----------|
|  uid   |    int    |   主键，用户id |
|   name   |   varchar(30)  |   用户名 |
|  avatar_url   |   varchar(200)   |   用户头像url |