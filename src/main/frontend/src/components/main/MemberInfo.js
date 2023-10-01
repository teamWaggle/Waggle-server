import React, { useEffect, useState } from 'react';
import { getSimpleMemberInfo } from '../../apis/memberApi';
import jwt_decode from 'jwt-decode';
import Cookies from 'js-cookie';

import DefaultInfo from './DefaultInfo';
import LoginedInfo from './LoginedInfo';

function MemberInfo() {
    const [loginedMember, setLoginedMember] = useState(null);

    useEffect(() => {
        async function fetchMemberInfo() {
            try {
                const accessToken = Cookies.get('access_token');
                if (accessToken) {
                    const decodedToken = jwt_decode(accessToken);
                    const username = decodedToken.sub;
                    const response = await getSimpleMemberInfo(username);
                    setLoginedMember(response.data);
                }

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
