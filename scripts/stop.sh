#!/usr/bin/env bash

# 기존 Eginx엔스에 연결되어 있지 않지만, 실행 중이던 스프링 부트 종료
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH) # 현재 stop.sh가 속해있는 경로
source ${ABSDIR}/profile.sh # 해당 코드로 profile.sh 내의 함수 사용

IDLE_PORT=$(find_idle_port)

echo ">>> $IDLE_PORT 에서 구동중인 애플리케이션 PID 확인"
IDLE_PID=$(sudo lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  # # Nginx에 연결되어 있지는 않지만 현재 실행 중인 jar 를 Kill 합니다.
  echo ">>> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi