import React, { useEffect, useState } from 'react';
import defaultProfileImg from '../../images/defaultProfileImg.png';
import styles from '../../styles/MemberInfo.module.css';
import { getSimpleMemberInfo } from '../../apis/memberApi';

function MemberInfo() {
    const [memberSimpleDto, setMemberSimpleDto] = useState(null);

    useEffect(() => {
        async function fetchMemberInfo() {
            try {
                const response = await getSimpleMemberInfo('user1'); // 사용자 이름 전달
                setMemberSimpleDto(response.data);
            } catch (error) {
                console.error('회원 정보를 가져오는 중 오류 발생:', error);
            }
        }

        fetchMemberInfo();
    }, []);

    return (
        <div className={styles.container}>
            {!memberSimpleDto && (
                <div className={styles.profileSection}>
                    <div className={styles.profileImage}>
                        <img src={defaultProfileImg} alt="프로필 사진" />
                    </div>
                    <div className={styles.profileText}>
                        <div className={styles.profileName}>꼬리를 흔들어주세요!</div>
                    </div>
                </div>
            )}

            <button
                className={`${styles.button}`}
                onClick={() => window.location.href = '/member/sign-in'}
                type="button"
            >
                Waggle 로그인
            </button>

            <div className={styles.linkSection}>
                <a href="/" className={styles.link}>
                    <p>아이디・비밀번호 찾기</p>
                </a>
                <a href="/member/sign-up" className={styles.link}>
                    <p>회원가입</p>
                </a>
            </div>
        </div>
    );
}

export default MemberInfo;
