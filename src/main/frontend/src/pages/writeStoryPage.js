import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getSimpleMemberInfo } from '../apis/memberApi'
import jwt_decode from 'jwt-decode';
import Cookies from 'js-cookie';
import Preview from '../components/story/Preview';
import styles from '../styles/writeStoryPage.module.css';
import '../styles/globalStyles.css';
import ImageComponent from '../components/ImageComponent';
import imageComponentStyles from '../styles/ImageComponent.module.css';
import defaultProfileImg from '../images/defaultProfileImg.png';

import { writeStoryApi } from '../apis/storyApi'

function WriteStory() {
    const [loginedMember, setLoginedMember] = useState(null);
    const [content, setContent] = useState('');
    const [imageFiles, setImageFiles] = useState(null);

    const navigate = useNavigate();


    const handleUpload = async () => {
        try {

            const formData = new FormData();
            formData.append('thumbnail', imageFiles[0]);
            imageFiles.forEach((file, index) => {
                formData.append('multipartFiles', file);
            });
            formData.append('storyWriteDto', new Blob([JSON.stringify({ content })], { type: 'application/json' }));
            const response = await writeStoryApi(formData);
            console.log(response.data);
            navigate('/');
        } catch (error) {
            console.error('스토리 업로드 중 오류 발생:', error);
        }
    };


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
                                <div className={styles.profileImgContainer}>
                                    {loginedMember ? (
                                        loginedMember.profileImg && loginedMember.profileImg.storeFileName
                                            ? <ImageComponent filename={loginedMember.profileImg.storeFileName} className={imageComponentStyles.mainProfileImg} alt="프로필사진" />
                                            : <img src={defaultProfileImg} className={styles.profileImg} alt="프로필 사진" />

                                    ) : (
                                        <div>로딩 중...</div>
                                    )}
                                </div>
                                {loginedMember && <div>{loginedMember.username}</div>}
                            </div>
                        </div>
                        <div className={styles.thumbnail}>
                            <Preview setImageFiles={setImageFiles} />
                        </div>
                    </div>
                    <div className={styles.contentContainer}>
                        <textarea
                            className={styles.content}
                            placeholder="자유롭게 글을 작성해 보세요."
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                        ></textarea>
                        <div className={styles.buttonContainer}>
                            <button className={styles.uploadButton} onClick={handleUpload}>스토리 업로드</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default WriteStory;