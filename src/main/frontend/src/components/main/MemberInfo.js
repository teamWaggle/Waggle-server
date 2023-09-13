import React, { useEffect, useState } from 'react';
import { getSimpleMemberInfo } from '../../apis/memberApi';

import DefaultInfo from './DefaultInfo';
import LoginedInfo from './LoginedInfo';

function MemberInfo() {
    const [loginedMember, setLoginedMember] = useState(null);

    useEffect(() => {
        async function fetchMemberInfo() {
            try {
                const response = await getSimpleMemberInfo('user'); // TODO 현재 로그인 된 사용자 이름 전달
                setLoginedMember(response.data);
            } catch (error) {
                console.error('회원 정보를 가져오는 중 오류 발생:', error);
            }
        }

        fetchMemberInfo();
    }, []);

    return (
        <div>
            {loginedMember ? (
                <LoginedInfo loginedMember={loginedMember} />
            ) : (
                <DefaultInfo />
            )}
        </div>
    );
}

export default MemberInfo;
