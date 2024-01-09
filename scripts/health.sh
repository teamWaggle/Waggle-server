#!/usr/bin/env bash

# start.sh로 실행시킨 프로젝트가 정상적으로 실행됐는지 체크
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo ">>> Health Check Start!"
echo ">>> IDLE_PORT: $IDLE_PORT"
echo ">>> curl -s http://localhost:$IDLE_PORT/profile"
sleep 10

# for 문 10번 돌기
for RETRY_COUNT in {1..10}
do
  # 현재 문제 없이 잘 실행되고 있는 요청을 보내봅니다.
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  # 해당 결과의 줄 수를 숫자로 리턴합니다.
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then # $up_count >= 1 ("set" 문자열이 있는지 검증)
      echo ">>> Health check 성공"
      switch_proxy # switch.sh 실행
      break
  else
      echo ">>> Health check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
      echo ">>> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo ">>> Health check 실패. "
    echo ">>> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo ">>> Health check 연결 실패. 재시도..."
  sleep 10
done