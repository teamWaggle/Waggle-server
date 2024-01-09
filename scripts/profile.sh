#!/usr/bin/env bash

# 쉬고 있는 profile 찾기
# set1이 사용중이면 set2가 쉬고 있으며, 반대편 set1이 쉬고 있다.
function find_idle_profile()
{
  RESPONSE_CODE=$(sudo curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(sudo curl -s http://localhost/profile)
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2 # Nginx와 연결되지 않은 profile
  else
    IDLE_PROFILE=real1
  fi

  # bash script는 return 기능이 없기 떄문에,
  # echo를 통해서 출력하면 이 값을 클라이언트가 사용할 수 있습니다.
  echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
    echo "8081" # 여기도 마찬가지로 return 기능의 느낌
  else
    echo "8082"
  fi
}