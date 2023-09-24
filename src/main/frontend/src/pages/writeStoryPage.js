import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getSimpleMemberInfo } from '../apis/memberApi'
import jwt_decode from 'jwt-decode';
import Cookies from 'js-cookie';
import Preview from '../components/story/Preview';
import styles from '../styles/writeStoryPage.module.css';
import '../styles/globalStyles.css';

import { writeStoryApi } from '../apis/storyApi'

function WriteStory() {
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
        <div className="page-container">
            <div className={styles.mainContainer}>
                <Link to="/" className={styles.logo}>
                    <div>Waggle</div>
                </Link>
                <div className={styles.storyContainer}>
                    <div className={styles.info}>
                        <div className={styles.infoHeader}>
                            <div className={styles.profile}>
                                {loginedMember ? (
                                    loginedMember.profileImg ? (
                                        <div className={styles.profileImgContainer}>
                                            <img className={styles.profileImg}
                                                src={`/images/${loginedMember.profileImg.getStoreFileName()}`}
                                                alt="프로필 사진"
                                            />
                                        </div>
                                    ) : (
                                        <div className={styles.profileImgContainer}>
                                            <img className={styles.profileImg}
                                                src="/images/defaultProfileImg.png"
                                                alt="프로필 사진"
                                            />
                                        </div>
                                    )
                                ) : (
                                    <div>로딩 중...</div>
                                )}
                                {loginedMember && <div>{loginedMember.username}</div>}
                            </div>
                        </div>
                        <div className={styles.thumbnail}>
                            <Preview />
                        </div>
                    </div>
                    <div className={styles.contentContainer}>
                        <textarea className={styles.content}></textarea>
                        <div className={styles.buttonContainer}>
                            <button className={styles.uploadButton}>스토리 업로드</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default WriteStory;