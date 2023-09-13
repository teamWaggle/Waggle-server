import React from 'react';
import defaultProfileImg from '../../images/defaultProfileImg.png';
import styles from '../../styles/DefaultInfo.module.css';

function DefaultInfo() {

    return (
        <div className={styles.container}>
            <div className={styles.profileSection}>
                <div className={styles.profileImage}>
                    <img src={defaultProfileImg} alt="프로필 사진" />
                </div>
                <div className={styles.profileText}>
                    <div className={styles.profileName}>꼬리를 흔들어주세요!</div>
                </div>
            </div>

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

export default DefaultInfo;
