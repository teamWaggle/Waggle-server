import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { getStoryApi } from '../apis/storyApi'
import defaultProfileImg from '../images/defaultProfileImg.png';

import styles from '../styles/storyPage.module.css';
import '../styles/globalStyles.css';

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
                                {story.profileImg ? (
                                    <div className={styles.profileImgContainer}>
                                        <img className={styles.profileImg}
                                            src={`/images/${story.profileImg.getStoreFileName()}`}
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
                                )}
                                <div>{story.username}</div>
                            </div>

                            <div>{story.createdDate}</div>

                        </div>

                        <img
                            className={styles.thumbnail}
                            src={story.thumbnail}
                            alt="썸네일"
                        />
                    </div>
                    <p className={styles.content}>{story.content}</p>
                </div>
            </div>
        </div>
    );
}

export default Story;