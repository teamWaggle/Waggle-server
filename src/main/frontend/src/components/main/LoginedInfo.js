import React from 'react';
import defaultProfileImg from '../../images/defaultProfileImg.png';
import styles from '../../styles/LoginedInfo.module.css';
import ImageComponent from '../ImageComponent';
import imageComponentStyles from '../../styles/ImageComponent.module.css';

import { memberLogoutApi } from '../../apis/memberApi'

function LoginedInfo({ loginedMember }) {
    console.log('loginedMember = ', loginedMember);
    const handleLogout = () => {
        memberLogoutApi()
            .then(response => {
                console.log('Logout successful:', response);
                window.location.href = '/';
            })
            .catch(error => {
                console.log('Logout failed:', error);
            });
    };

    return (
        <div className={styles.container}>
            <div className={styles.profileSection}>
                <div className={styles.subProfileSection}>
                    <div className={styles.profileImage}>
                        {loginedMember.profileImg && loginedMember.profileImg.storeFileName
                            ? <ImageComponent filename={loginedMember.profileImg.storeFileName} className={imageComponentStyles.mainProfileImg} alt="프로필사진" />
                            : <img src={defaultProfileImg} alt="프로필 사진" />
                        }
                    </div>
                    <div className={styles.profileText}>
                        <div className={styles.profileNickname}>{loginedMember.nickname}</div>
                        <div className={styles.profileUsername}>@{loginedMember.username}</div>
                    </div>
                </div>
                <button
                    onClick={handleLogout}
                    className={styles.btnLogout}
                    type="button">로그아웃
                </button>
            </div>

            <button
                className={`${styles.button}`}
                onClick={() => window.location.href = '/story/write'}
                type="button"
            >
                스토리 작성하기
            </button>

        </div>
    );
}

export default LoginedInfo;
