#!/usr/bin/env bash

# 배포할 신규 버전 프로젝트를 stop.sh로 종료한 profile로 실행
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh # 해당 코드로 profile.sh 내의 함수 사용

REPOSITORY=/home/ec2-user/app/step3

echo ">>> Build 파일 복사"
echo ">>> cp $REPOSITORY/zip/build/libs/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/build/libs/*.jar $REPOSITORY/

echo ">>> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)    # jar 이름 꺼내오기

echo ">>> JAR Name: $JAR_NAME"
echo ">>> $JAR_NAME 에 실행 권한 추가"
chmod +x $JAR_NAME

echo ">>> $JAR_NAME 실행"
IDLE_PROFILE=$(find_idle_profile)

# 위에서 보았던 것처럼 $IDLE_PROFILE에는 set1 or set2가 반환되는데
# 반환되는 properties를 실행한다는 뜻.
echo ">>> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-security.yml,/home/ec2-user/app/application-s3.yml,/home/ec2-user/app/application-real-db.yml,classpath:/application-real.yml, classpath:/application-real1.yml, classpath:/application-real2.yml \
        -Dspring.profiles.active=$IDLE_PROFILE \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &