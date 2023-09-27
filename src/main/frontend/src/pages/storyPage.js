import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { getStoryApi } from '../apis/storyApi'
import defaultProfileImg from '../images/defaultProfileImg.png';

import styles from '../styles/storyPage.module.css';
import imageComponentStyles from '../styles/ImageComponent.module.css';
import '../styles/globalStyles.css';
import ImageComponent from '../components/ImageComponent';

function Story() {
    const { username, boardId } = useParams();
    const [story, setStory] = useState([]);

    useEffect(() => {
        const fetchStory = async () => {
            try {
                const response = await getStoryApi(username, boardId);
                const data = response.data;
                console.log('data = ', data);
                setStory(data);
            } catch (error) {
                console.error('API 요청 오류:', error);
            }
        };
        fetchStory();
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
                                    {story ? (
                                        story.profileImg && story.profileImg.storeFileName
                                            ? <ImageComponent filename={story.profileImg.storeFileName} className={imageComponentStyles.mainProfileImg} alt="프로필사진" />
                                            : <img src={defaultProfileImg} className={styles.profileImg} alt="프로필 사진" />

                                    ) : (
                                        <div>로딩 중...</div>
                                    )}
                                </div>
                                <div>{story.username}</div>
                            </div>
                            <div>{story.createdDate}</div>
                        </div>

                        <ImageComponent filename={story.thumbnail} className={imageComponentStyles.storyPageImg} alt="썸네일" />
                    </div>
                    <p className={styles.content}>{story.content}</p>
                </div>
            </div>
        </div>
    );
}

export default Story;