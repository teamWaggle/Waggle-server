import React from 'react';
import styles from '../../styles/SimpleStory.module.css';
import { useNavigate } from 'react-router-dom';

function SimpleStory(props) {
    const { story } = props;
    const navigate = useNavigate();

    const handleNavigation = () => {
        navigate(`/story/${story.username}/${story.id}`);
    };

    return (
        <div className={styles.simpleStory}>
            <div className={styles.info}>
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
                src={story.thumbnail}
                alt="썸네일"
                onClick={handleNavigation}
            />
        </div>
    );
}

export default SimpleStory;
