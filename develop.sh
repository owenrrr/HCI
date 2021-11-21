#!/bin/bash
project_path="/var/lib/jenkins/workspace/seiii/SpringbootDemo/target"
project_name="demo"
tomcat_home="/usr/local/apache-tomcat-8.5.63"
echo this is app : $project_name
echo tomcat : $tomcat_home
echo "删除war包"
rm $tomcat_home/webapps/ROOT.war

echo "复制war包"
mv $project_path/$project_name.war $tomcat_home/webapps/

echo "kill tomcat $tomcat_home"
ID=`ps -ef | grep java | grep $tomcat_home|awk '{print $2}'`
for id in $ID
do
    kill -9 $id
    echo "killed pid=$id"
done
echo "执行tomcat启动shell"
#防止jenkins构建完成后杀死tomcat进程
export BUILD_ID=dontKillMe
sh $tomcat_home/bin/startup.sh
#tail -f $tomcat_home/logs/catalina.out
exit 0
