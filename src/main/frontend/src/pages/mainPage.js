import React from 'react';
import Nav from '../components/Nav';
import StoryList from '../components/main/StoryList';
import MemberInfo from '../components/main/MemberInfo';

import styles from '../styles/mainpage.module.css'
import '../styles/globalStyles.css';

function Main() {
    return (
        <div className="page-container">
            <Nav />
            <div className={styles.mainContainer}>
                <StoryList />
                <MemberInfo />
            </div>
        </div>
    );
}

export default Main;