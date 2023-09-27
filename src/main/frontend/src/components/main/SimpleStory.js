import React from 'react';
import styles from '../../styles/SimpleStory.module.css';
import { useNavigate } from 'react-router-dom';
import ImageComponent from '../ImageComponent';
import imageComponentStyles from '../../styles/ImageComponent.module.css';
import defaultProfileImg from '../../images/defaultProfileImg.png';

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
                    <div className={styles.profileImgContainer}>
                        {story.profileImg && story.profileImg.storeFileName ?
                            <ImageComponent filename={story.profileImg.storeFileName} className={imageComponentStyles.mainProfileImg} alt="프로필사진" />
                            : <img src={defaultProfileImg} className={styles.profileImg} alt="프로필 사진" />
                        }
                    </div>
    
                    <div>{story.username}</div>
                </div>
                <div>{story.createdDate}</div>
            </div>

            <ImageComponent filename={story.thumbnail} className={imageComponentStyles.simpleStoryImg} onClick={handleNavigation} alt="썸네일" />

        </div >
    );
}

export default SimpleStory;
